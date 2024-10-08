package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.mapper.UserMapper;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 원래는 boolean 값을 반환한다고 함
    public void signUpProcess(SignUpDto dto) {

//      1. 중복 이메일 검증
        Boolean isExist = userRepository.existsByEmail(dto.getEmail());

        if (isExist) return;

//      2. 중복 이메일이 없으면 회원가입 절차 실행

//      2 - 1. 비밀번호 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

//      2 - 2. dto 엔티티화
        User user = UserMapper.INSTANCE.toEntity(dto);

//      2 - 3. user 객체에 인코딩된 비밀번호 값을 설정
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}
