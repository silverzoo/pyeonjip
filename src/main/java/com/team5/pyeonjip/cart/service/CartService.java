package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartDetailDto;
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

    @Transactional
    public List<CartDetailDto> syncCart(Long userId, List<CartDetailDto> localCartItems) {
        // 서버에서 현재 장바구니 아이템 조회
        List<Cart> serverCartItems = cartRepository.findByUserId(userId);

        // 서버 아이템을 Map으로 변환
        Map<Long, Cart> serverItemMap = serverCartItems.stream()
                .collect(Collectors.toMap(Cart::getOptionId, Function.identity()));

        // 로컬 카트 아이템을 순회하여 동기화
        for (CartDetailDto localItem : localCartItems) {
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

    public List<CartDetailDto> getCartItemsByUserId(Long userId) {
        List<Cart> serverCartItems = cartRepository.findByUserId(userId);
        // Cart Entity -> Cart DTO
        List<CartDetailDto> cartDetailDtos = serverCartItems.stream()
                .map(cart -> {
                    return getCartDetailDto(cart.getOptionId());
                })
                .collect(Collectors.toList());
        return cartDetailDtos;
    }

    // Mapper 대신 사용
    // Cart Entity -> CartDTO
    public CartDto getCartDto(Long optionId) {
        CartDto cartDto = new CartDto();
        cartDto.setOptionId(optionId);
        cartDto.setQuantity(1L);
        return cartDto;
    }

    public List<CartDetailDto> getCartDetailsByCartDto(List<CartDto> cartDtos) {
        List<CartDetailDto> detailDtos = cartDtos.stream().map(cartDto -> {
            ProductDetail productDetail = productDetailRepository.findById(cartDto.getOptionId())
                    .orElseThrow(() -> new  ResourceNotFoundException("[ProductDetail not found, id : " + cartDto.getOptionId() + "]"));
            //ProductResponse product = productService.getProductById(productDetail.getProduct().getId());
            CartDetailDto cartDetailDto = new CartDetailDto();
            cartDetailDto.setOptionId(productDetail.getId());
            cartDetailDto.setName(productDetail.getProduct().getName());
            cartDetailDto.setOptionName(productDetail.getName());
            cartDetailDto.setPrice(productDetail.getPrice());
            cartDetailDto.setQuantity(cartDto.getQuantity());
            cartDetailDto.setMaxQuantity(productDetail.getQuantity());
            cartDetailDto.setUrl(productDetail.getMainImage());


            return cartDetailDto;
        }).toList();
        return detailDtos;
        }


    // Cart Entity -> CartDetailDTO
    public CartDetailDto getCartDetailDto(Long optionId) {

        ProductDetail productDetail = productDetailRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("[ProductDetail not found, id : " + optionId + "]"));
        ProductResponse product = productService.getProductById(productDetail.getProduct().getId());
        CartDetailDto detailDto = new CartDetailDto();
        //detailDto.setUserId(userId);
        detailDto.setOptionId(optionId);
        detailDto.setName(product.getName());
        detailDto.setOptionName(productDetail.getName());
        detailDto.setPrice(productDetail.getPrice());
        detailDto.setQuantity(1L);
        detailDto.setMaxQuantity(productDetail.getQuantity());
        detailDto.setUrl(productDetail.getMainImage());

        return detailDto;
    }
}
