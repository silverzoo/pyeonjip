package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    // 추후 팀원과 협의해서 리펙토링 할 예정
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductDetailRepository productDetailRepository;

    public List<CartDto> syncCart(Long userId, List<CartDto> localCartItems) {
        // 서버에서 현재 장바구니 아이템 조회
        List<Cart> serverCartItems = cartRepository.findByUserId(userId);

        // 서버 아이템을 Map으로 변환
        Map<Long, Cart> serverItemMap = serverCartItems.stream()
                .collect(Collectors.toMap(Cart::getOptionId, Function.identity()));

        // 로컬 카트 아이템을 순회하여 동기화
        for (CartDto localItem : localCartItems) {
            Cart serverItem = serverItemMap.get(localItem.getOptionId());

            if (serverItem != null) {
                // 서버에 아이템이 존재하면 수량 동기화
                serverItem.setQuantity(localItem.getQuantity());
                try {
                    cartRepository.save(serverItem);
                } catch (Exception e) {
                    // 로깅 및 예외 처리
                    log.error("Failed to save cart item: {}", localItem.getOptionId(), e);
                }
            } else {
                // 서버에 없는 경우 새로 추가
                Cart newCartItem = new Cart();
                newCartItem.setUserId(userId);
                newCartItem.setOptionId(localItem.getOptionId());
                newCartItem.setQuantity(localItem.getQuantity());
                try {
                    cartRepository.save(newCartItem);
                } catch (Exception e) {
                    // 로깅 및 예외 처리
                    log.error("Failed to create new cart item: {}", localItem.getOptionId(), e);
                }
            }
        }

        // 로컬 카트에 존재하지 않는 서버 항목을 삭제
        for (Cart serverItem : serverCartItems) {
            if (!localCartItems.stream().anyMatch(localItem -> localItem.getOptionId().equals(serverItem.getOptionId()))) {
                try {
                    cartRepository.delete(serverItem);
                } catch (Exception e) {
                    // 로깅 및 예외 처리
                    log.error("Failed to delete cart item: {}", serverItem.getOptionId(), e);
                }
            }
        }

        return localCartItems;
    }


    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }


    @Transactional
    public void deleteCartItem(Long userId, Long optionId) {
        cartRepository.deleteByUserIdAndOptionId(userId, optionId);
    }

    public boolean existsByUserIdAndOptionId(Long userId, Long optionId) {
        return cartRepository.existsByUserIdAndOptionId(userId, optionId);
    }


    public List<CartDto> getCartItemsByUserId(Long userId) {
        List<Cart> serverCartItems = cartRepository.findByUserId(userId);
        // Cart Entity -> Cart DTO
        List<CartDto> cartDtos = serverCartItems.stream()
                .map(cart -> {
                    return getCartDto(userId, cart.getOptionId());
                })
                .collect(Collectors.toList());
        return cartDtos;
    }

    public CartDto getCartDto(Long userId, Long optionId) {

        ProductDetail productDetail = productDetailRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("[ProductDetail not found, id : " + optionId + "]"));
        ProductResponse product = productService.getProductById(productDetail.getProduct().getId());
        CartDto dto = new CartDto();

        dto.setUserId(userId);
        dto.setOptionId(optionId);
        dto.setName(product.getName());
        dto.setOptionName(productDetail.getName());
        dto.setPrice(productDetail.getPrice());
        dto.setQuantity(1L);
        dto.setMaxQuantity(productDetail.getQuantity());
        dto.setUrl(productDetail.getMainImage());

        return dto;
    }
}
