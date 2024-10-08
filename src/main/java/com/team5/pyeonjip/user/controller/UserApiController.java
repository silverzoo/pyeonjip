package com.team5.pyeonjip.user.controller;

import com.team5.pyeonjip.user.dto.SignUpDto;
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


    /* CREATE */

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody SignUpDto dto) {

        try {
            // UserService의 유저 생성 메서드 실행
            userService.createUser(dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(URI.create("/"))
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    /* READ */

    // 모든 유저 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        try {
            // UserService의 전체 유저 조회 메서드 실행
            return ResponseEntity.ok(userService.findAllUsers());
        } catch (Exception e) {

            // Todo: 추후 구체적으로 작성할 것
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


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


    /* UPDATE */

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


    /* DELETE */

    @DeleteMapping("/delete/{userId}")
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

}
