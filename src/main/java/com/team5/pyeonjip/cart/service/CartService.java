package com.team5.pyeonjip.cart.service;

import com.team5.pyeonjip.cart.dto.CartItemResponseDTO;
import com.team5.pyeonjip.cart.dto.Options;
import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.entity.CartItem;
import com.team5.pyeonjip.cart.repository.CartItemRepository;
import com.team5.pyeonjip.cart.repository.CartRepository;
import com.team5.pyeonjip.product.dto.ProductResponse;
import com.team5.pyeonjip.product.entity.Product;
import com.team5.pyeonjip.product.entity.ProductDetail;
import com.team5.pyeonjip.product.entity.ProductImage;
import com.team5.pyeonjip.product.repository.ProductDetailRepository;
import com.team5.pyeonjip.product.repository.ProductImageRepository;
import com.team5.pyeonjip.product.repository.ProductRepository;
import com.team5.pyeonjip.product.service.ProductService;
import com.team5.pyeonjip.user.repository.UserRepository;
import com.team5.pyeonjip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

   public Cart getCartByUserId(Long userId) {
       Cart target = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
       return target;
   }

   public Cart saveCart(Cart cart){
       return cartRepository.save(cart);
   }

   public void clearCart(Long userId) {
       cartRepository.deleteByUserId(userId);
   }

//   public List<CartItemResponseDTO> getCartItems(Long userId) {
//
//   }

   public CartItemResponseDTO getProduct(Long productId) {
       ProductResponse response = productService.getProductById(productId);
       CartItemResponseDTO dto = new CartItemResponseDTO();
       List<Options> optionsList = new ArrayList<>();
       dto.setId(response.getId());
       dto.setName(response.getName());

       for (int i = 0; i < response.getProductDetails().size(); i++) {
           // 두 List 사이즈가 같을거라는 믿음...예외처리 예정
           ProductResponse.ProductDetailResponse detail = response.getProductDetails().get(i);
           ProductResponse.ProductImageResponse image = response.getProductImages().get(i);
           Options options = new Options();
           options.setOptionName(detail.getName());
           options.setOptionPrice(detail.getPrice());
           options.setMaxQuantity(detail.getQuantity());
           options.setUrl(image.getImageUrl());

           optionsList.add(options);
       }
       dto.setProductDetails(optionsList);
       return dto;
   }
}
