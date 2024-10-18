package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartDetailDto;
import com.team5.pyeonjip.cart.dto.CartDto;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.global.exception.GlobalException;
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
import static com.team5.pyeonjip.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductDetailRepository productDetailRepository;

    // 조회
    public List<CartDto> getCartItemsByEmail(String email) {
        List<Cart> serverCartItems = cartRepository.findAllByEmail(email);
        // Cart Entity -> Cart DTO
        return serverCartItems.stream()
                .map(cart -> {
                    CartDto cartDto = new CartDto();
                    cartDto.setOptionId(cart.getOptionId());
                    cartDto.setQuantity(cart.getQuantity());
                    return cartDto;
                })
                .collect(Collectors.toList());
    }

    // CartDto -> CartDetailDto
    public List<CartDetailDto> mapCartDtosToCartDetails(List<CartDto> cartDtos) {
        return cartDtos.stream().map(cartDto -> {
            ProductDetail productDetail = productDetailRepository.findById(cartDto.getOptionId())
                    .orElseThrow(() -> new GlobalException(PRODUCT_DETAIL_NOT_FOUND));
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
    }

    @Transactional
    public CartDto addCartDto(CartDto cartDto, String email) {
        Cart existingCart = cartRepository.findByEmailAndOptionId(email, cartDto.getOptionId());
        // 존재할 경우 quantity++
        if (existingCart != null) {
            // Validation
            existingCart.setQuantity(existingCart.getQuantity() + 1);
            Cart updatedCart = cartRepository.save(existingCart);

            CartDto updatedCartDto = new CartDto();
            updatedCartDto.setOptionId(updatedCart.getOptionId());
            updatedCartDto.setQuantity(updatedCart.getQuantity());

            return updatedCartDto;
        } else {
            // 존재하지 않는 경우
            Cart newCart = new Cart();
            newCart.setEmail(email);
            newCart.setOptionId(cartDto.getOptionId());
            newCart.setQuantity(cartDto.getQuantity() != null ? cartDto.getQuantity() : 1);  // 기본 수량 1

            Cart savedCart = cartRepository.save(newCart);

            // 저장된 Cart 엔티티를 CartDto로 변환하여 반환
            CartDto savedCartDto = new CartDto();
            savedCartDto.setOptionId(savedCart.getOptionId());
            savedCartDto.setQuantity(savedCart.getQuantity());

            return savedCartDto;
        }
    }

    @Transactional
    public CartDto updateCartItemQuantity(String email, Long optionId, CartDto dto) {
        Cart target = cartRepository.findByEmailAndOptionId(email, optionId);

        //Validation
        ProductDetail productDetail = productDetailRepository.findById(optionId)
                .orElseThrow(() -> new GlobalException(PRODUCT_DETAIL_NOT_FOUND));
        if (dto.getQuantity() > productDetail.getQuantity()) {
            throw new GlobalException(OUT_OF_STOCK);
        }

        target.setQuantity(dto.getQuantity());
        cartRepository.save(target);
        return dto;
    }

    @Transactional
    public void deleteCartItemByEmailAndOptionId(String email, Long optionId) {
        cartRepository.deleteByEmailAndOptionId(email, optionId);
    }

    @Transactional
    public void deleteAllCartItems(String email) {
        cartRepository.deleteAllByEmail(email);
    }

    @Transactional
    public void deleteCartItemByOptionId(Long optionId) {
        cartRepository.deleteByOptionId(optionId);
    }

    @Transactional
    public List<CartDto> sync(String email, List<CartDto> localCartItems) {
        // 서버에서 현재 장바구니 아이템 조회
        List<Cart> serverCartItems = cartRepository.findAllByEmail(email);

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
                newCartItem.setEmail(email);
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
        /*
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
        */
        return localCartItems;
    }
}


