package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.cart.entity.Cart;
import com.team5.pyeonjip.cart.service.CartService;
import com.team5.pyeonjip.coupon.entity.Coupon;
import com.team5.pyeonjip.coupon.repository.CouponRepository;
import com.team5.pyeonjip.product.entity.Product;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CouponRepository couponRepository;
    private final CartService cartService;

    // 테스트용 List
    List<itemTest> items = new ArrayList<>();
    itemTest itemTest1 = new itemTest(1, "페브릭 소파", 250000, "https://godomall.speedycdn.net/dd670437e41588ed97aae4c43ebb109a/goods/1000005635/image/detail/register_detail_089.jpg", "소파");
    itemTest itemTest2 = new itemTest(2, "월넛 모션데스크", 500000, "https://www.emons.co.kr/shop/data/goods/66qo7IWY642w7Iqk7YGs_1400.jpg", "책상");
    itemTest itemTest3 = new itemTest(3, "5단 아이보리 책장", 100000, "https://static.hyundailivart.co.kr/upload_mall/goods/P100001245/GM40404792_img.jpg/dims/resize/x890/optimize", "책장");
    itemTest itemTest4 = new itemTest(4, "음표 조명", 30000, "https://cafe24.poxo.com/ec01/jangpaulkr/Xeym8gXyw/uNs04t9Tz1DnvB60CCR9J8JOszC+e7D8cjj75tYI5LpPsCXBLoBM50y3zQXUybY8eJLnC6bSYxHA==/_/web/product/big/202008/ed42f11b708b33283387ba663fa6be77.jpg", "조명");



    // 테스트 샌드박스용 페이지
    @GetMapping("/sandbox")
    public List<itemTest> sandbox() {
        items.clear();
        items.add(itemTest1);
        items.add(itemTest2);
        items.add(itemTest3);
        items.add(itemTest4);
        return items;
    }

    // 장바구니 페이지
    @GetMapping
    public List<Coupon> cart(){
        List<Coupon> coupons = couponRepository.findAll();

        return coupons;
    }

    @GetMapping("/sync")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId){
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/save")
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }
}

@Data
class itemTest {

    private Integer id;
    private String name;
    private Integer price;
    private String image;
    private String category;

    public itemTest(Integer id, String name, Integer price, String image, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }
}
