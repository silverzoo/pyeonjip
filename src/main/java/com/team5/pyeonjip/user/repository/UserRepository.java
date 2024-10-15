package com.team5.pyeonjip.user.repository;

import com.team5.pyeonjip.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 회원가입 시, 이메일이 중복되는지 확인
    Boolean existsByEmail(String email);


    // 로그인 시, DB에서 유저 정보를 조회
    User findByEmail(String email);


    // 계정 찾기 시, 이름과 전화번호에 해당되는 이메일이 있는지 확인
    Boolean existsByNameAndPhoneNumber(String name, String phoneNumber);

    User findByNameAndPhoneNumber(String name, String phoneNumber);
}
