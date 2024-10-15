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
    public List<CartDto> getCartItemsByUserId(Long userId) {
        List<Cart> serverCartItems = cartRepository.findAllByUserId(userId);
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
    public CartDto addCartDto(CartDto cartDto, Long userId) {
        Cart existingCart = cartRepository.findByUserIdAndOptionId(userId, cartDto.getOptionId());
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
            newCart.setUserId(userId);
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
    public CartDto updateCartItemQuantity(Long userId, Long optionId, CartDto dto) {
        Cart target = cartRepository.findByUserIdAndOptionId(userId, optionId);

        //Validation
        ProductDetail productDetail = productDetailRepository.findById(optionId)
                .orElseThrow(() -> new GlobalException(PRODUCT_DETAIL_NOT_FOUND));
        if(dto.getQuantity() > productDetail.getQuantity()){
            throw new GlobalException(OUT_OF_STOCK);
        }

        target.setQuantity(dto.getQuantity());
        cartRepository.save(target);
        return dto;
    }

    @Transactional
    public void deleteCartItemByUserIdAndOptionId(Long userId, Long optionId) {
        cartRepository.deleteByUserIdAndOptionId(userId,optionId);
    }

    @Transactional
    public void deleteAllCartItems(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteCartItemByOptionId(Long optionId) {
        cartRepository.deleteByOptionId(optionId);
    }
}
