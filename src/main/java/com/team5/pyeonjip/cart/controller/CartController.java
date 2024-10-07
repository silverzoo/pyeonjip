package com.team5.pyeonjip.cart.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    List<Item_Test> items = new ArrayList<>();
    Item_Test itemTest1 = new Item_Test(1, "페브릭 소파", 250000, "https://godomall.speedycdn.net/dd670437e41588ed97aae4c43ebb109a/goods/1000005635/image/detail/register_detail_089.jpg", "소파");
    Item_Test itemTest2 = new Item_Test(2, "월넛 모션데스크", 500000, "https://www.emons.co.kr/shop/data/goods/66qo7IWY642w7Iqk7YGs_1400.jpg", "책상");
    Item_Test itemTest3 = new Item_Test(3, "5단 아이보리 책장", 100000, "https://static.hyundailivart.co.kr/upload_mall/goods/P100001245/GM40404792_img.jpg/dims/resize/x890/optimize", "책장");
    Item_Test itemTest4 = new Item_Test(4, "음표 조명", 30000, "https://cafe24.poxo.com/ec01/jangpaulkr/Xeym8gXyw/uNs04t9Tz1DnvB60CCR9J8JOszC+e7D8cjj75tYI5LpPsCXBLoBM50y3zQXUybY8eJLnC6bSYxHA==/_/web/product/big/202008/ed42f11b708b33283387ba663fa6be77.jpg", "조명");

    List<Coupon> coupons = new ArrayList<>();
    Coupon coupon1 = new Coupon("test", 10);
    Coupon coupon2 = new Coupon("asdf", 20);
    Coupon coupon3 = new Coupon("qwer", 30);

    @GetMapping("/sandbox")
    public List<Item_Test>  sandbox() {
        items.clear();
        items.add(itemTest1);
        items.add(itemTest2);
        items.add(itemTest3);
        items.add(itemTest4);
        return items;
    }

    @GetMapping
    public List<Coupon> cart(){
        coupons.add(coupon1);
        coupons.add(coupon2);
        coupons.add(coupon3);
        return coupons;
    }
}

@Data
class Item_Test {

    private Integer id;
    private String name;
    private Integer price;
    private String image;
    private String category;

    public Item_Test(Integer id, String name, Integer price, String image, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }
}

@Data
class Coupon {

    private String promocode;
    private Integer percent;

    public Coupon(String promocode, Integer percent) {
        this.promocode = promocode;
        this.percent = percent;
    }
}
