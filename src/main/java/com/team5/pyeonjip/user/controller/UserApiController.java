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

        try {
            // UserService의 유저 생성 메서드 실행
            userService.signUpProcess(dto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoDto> mypage(@RequestParam String email) {

        try {

            return ResponseEntity.ok(userService.getUserInfo(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


//    // 모든 유저 조회
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//
//        try {
//            // UserService의 전체 유저 조회 메서드 실행
//            return ResponseEntity.ok(userService.findAllUsers());
//        } catch (Exception e) {
//
//            // Todo: 추후 구체적으로 작성할 것
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(null);
//        }
//    }


    // 단일 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {

        try {
            // UserService의 단일 유저 조회 메서드 실행
            return ResponseEntity.ok(userService.findUser(userId));
        } catch (Exception e) {
            // Todo: 위와 마찬가지
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    // 유저 삭제
    @PutMapping("/information/{userId}")
    public ResponseEntity<String> updateUserInfo(@PathVariable("userId") Long userId, @RequestBody UserUpdateDto dto) {

        try {
            // UserService의 유저 정보 수정 메서드 실행
            userService.updateUserInfo(userId, dto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    // 유저 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {

        try {
            // UserService의 유저 삭제 메서드 실행
            userService.deleteUser(userId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    // 계정 찾기
    @GetMapping("/find")
    public ResponseEntity<String> findAccount(@RequestParam String name, @RequestParam String phoneNumber) {

        try {
            // UserService의 계정 찾기 메서드 실행
            UserFindAccountDto dto = new UserFindAccountDto(name, phoneNumber);
            User user = userService.findAccount(dto);

            // 정보에 해당되는 계정이 없을 경우
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("계정을 찾을 수 없습니다.");
            }

            // 정보에 해당되는 계정이 있으면 200 응답과 이메일을 반환
            return ResponseEntity.ok(user.getEmail());
        } catch (Exception e) {

            // 계정 찾기에 실패했을 경우 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("계정 찾기에 실패했습니다.");
        }
    }

}
