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

    @PostMapping(value = "/new")
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
    public ResponseEntity<?> getAllUsers() {

        try {
            // UserService의 전체 유저 조회 메서드 실행
            List<User> users = userService.findAllUsers();

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    // 단일 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {

        try {
            // UserService의 단일 유저 조회 메서드 실행
            User user = userService.findUser(id);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    /* UPDATE */

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserInfo(@PathVariable("id") Long id, @RequestBody UserUpdateDto dto) {

        try {
            // UserService의 유저 정보 수정 메서드 실행
            userService.updateUserInfo(id, dto);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("정보가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    /* DELETE */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

        try {
            // UserService의 유저 삭제 메서드 실행
            userService.deleteUser(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("사용자 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
