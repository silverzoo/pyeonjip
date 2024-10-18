package com.team5.pyeonjip.user.controller;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.dto.UserFindAccountDto;
import com.team5.pyeonjip.user.dto.UserInfoDto;
import com.team5.pyeonjip.user.dto.UserUpdateDto;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserApiController {

    private final UserService userService;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto dto) {

        boolean isSignUpSuccessful = userService.signUpProcess(dto);
        if (isSignUpSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoDto> mypage(@RequestParam String email) {

        return ResponseEntity.ok(userService.getUserInfo(email));
    }


    // 단일 유저 조회
//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
//
//        return ResponseEntity.ok(userService.findUser(userId));
//    }


    // 단일 유저 조회(이메일)
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {

        return ResponseEntity.ok(userService.findUserByEmail(email));
    }


    // 유저 정보 업데이트
    @PatchMapping("/{email}")
    public ResponseEntity<Void> updateUserInfo(@PathVariable("email") String email, @RequestBody UserUpdateDto dto) {

        userService.updateUserInfo(email, dto);
        return ResponseEntity.noContent().build();
    }


    // 유저 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email) {

        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }


    // 계정 찾기
    @GetMapping("/find")
    public ResponseEntity<String> findAccount(@RequestParam String name, @RequestParam String phoneNumber) {

        UserFindAccountDto dto = new UserFindAccountDto(name, phoneNumber);
        User user = userService.findAccount(dto);

        return ResponseEntity.ok(user.getEmail());
    }

}
