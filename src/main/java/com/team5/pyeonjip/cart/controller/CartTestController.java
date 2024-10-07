package com.team5.pyeonjip.cart.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/cart")
//public class CartTestController {
//
//
//    List<Item_Test> items = new ArrayList<>();
//    Item_Test itemTest1 = new Item_Test(1, "페브릭 소파", 250000, "https://godomall.speedycdn.net/dd670437e41588ed97aae4c43ebb109a/goods/1000005635/image/detail/register_detail_089.jpg", "소파");
//    Item_Test itemTest2 = new Item_Test(2, "월넛 모션데스크", 500000, "https://www.emons.co.kr/shop/data/goods/66qo7IWY642w7Iqk7YGs_1400.jpg", "책상");
//    Item_Test itemTest3 = new Item_Test(3, "5단 아이보리 책장", 100000, "https://static.hyundailivart.co.kr/upload_mall/goods/P100001245/GM40404792_img.jpg/dims/resize/x890/optimize", "책장");
//
//    @GetMapping("/sandbox")
//    public String home(Model model) {
//        items.clear();
//        items.add(itemTest1);
//        items.add(itemTest2);
//        items.add(itemTest3);
//        model.addAttribute("items", items);
//
//        return "/cart/sandBox";
//    }
//
//
//    @GetMapping("/test")
//    public String signIn(Model model) {
//
//        items.clear();
//        items.add(itemTest1);
//        items.add(itemTest2);
//        items.add(itemTest3);
//
//        model.addAttribute("items", items);
//
//        List<Coupon> coupons = new ArrayList<>();
//        Coupon coupon1 = new Coupon("test", 10);
//        Coupon coupon2 = new Coupon("asdf", 20);
//        Coupon coupon3 = new Coupon("qwer", 30);
//        coupons.add(coupon1);
//        coupons.add(coupon2);
//        coupons.add(coupon3);
//
//        model.addAttribute("coupons", coupons);
//
//        return "/cart/cart";
//    }
//}


