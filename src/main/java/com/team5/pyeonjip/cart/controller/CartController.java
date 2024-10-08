package com.team5.pyeonjip.cart.controller;

import com.team5.pyeonjip.coupon.entity.Coupon;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    // 테스트용 List
    List<itemTest> items = new ArrayList<>();
    itemTest itemTest1 = new itemTest(1, "페브릭 소파", 250000, "https://godomall.speedycdn.net/dd670437e41588ed97aae4c43ebb109a/goods/1000005635/image/detail/register_detail_089.jpg", "소파");
    itemTest itemTest2 = new itemTest(2, "월넛 모션데스크", 500000, "https://www.emons.co.kr/shop/data/goods/66qo7IWY642w7Iqk7YGs_1400.jpg", "책상");
    itemTest itemTest3 = new itemTest(3, "5단 아이보리 책장", 100000, "https://static.hyundailivart.co.kr/upload_mall/goods/P100001245/GM40404792_img.jpg/dims/resize/x890/optimize", "책장");
    itemTest itemTest4 = new itemTest(4, "음표 조명", 30000, "https://cafe24.poxo.com/ec01/jangpaulkr/Xeym8gXyw/uNs04t9Tz1DnvB60CCR9J8JOszC+e7D8cjj75tYI5LpPsCXBLoBM50y3zQXUybY8eJLnC6bSYxHA==/_/web/product/big/202008/ed42f11b708b33283387ba663fa6be77.jpg", "조명");

    // 테스트용 List
    List<Coupon> coupons = new ArrayList<>();
    Coupon coupon1 = new Coupon("test",10L,true, LocalDateTime.now().plusWeeks(1));
    Coupon coupon2 = new Coupon("asdf", 20L, true, LocalDateTime.now().minusDays(1));
    Coupon coupon3 = new Coupon("qwer", 30L, false,LocalDateTime.now().plusWeeks(1));

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
        coupons.add(coupon1);
        coupons.add(coupon2);
        coupons.add(coupon3);
        return coupons;
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
