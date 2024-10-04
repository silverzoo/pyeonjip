//package com.team5.pyeonjip.user.controller;
//
//import com.team5.pyeonjip.user.entity.User;
//import com.team5.pyeonjip.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RequestMapping("/user")
//@Controller
//public class UserViewController {
//
//    private final UserService userService;
//
//    /* READ */
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public User getUser(@PathVariable("id") Long id) {
//        return userService.getUser(id);
//    }
//}
