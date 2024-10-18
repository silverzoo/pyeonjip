//package com.team5.pyeonjip.user.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//@RestController
//public class SecurityMainController {
//
//    // HomeController와 매핑 중복으로 / -> /security
//    @GetMapping("/securitymain-test")
//    public String mainP() {
//
//        // name을 email로 사용했기 때문에, email이 정상적으로 반환된다.
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
//        GrantedAuthority auth = iter.next();
//        String role = auth.getAuthority();
//
//        return "Main Controller" + email + role;
//    }
//
//    @GetMapping("/mytest")
//    public String test() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        return "Test - email: " + email;
//    }
//}
