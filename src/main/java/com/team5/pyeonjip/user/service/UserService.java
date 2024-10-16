package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.dto.UserFindAccountDto;
import com.team5.pyeonjip.user.dto.UserInfoDto;
import com.team5.pyeonjip.user.mapper.UserMapper;
import com.team5.pyeonjip.user.dto.UserUpdateDto;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

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


    // 마이페이지 조회
    public UserInfoDto getUserInfo(String email) {

        User user = userRepository.findByEmail(email);

        return UserInfoDto.builder().name(user.getName())
                                    .email(user.getEmail())
                                    .phoneNumber(user.getPhoneNumber())
                                    .address(user.getAddress())
                                    .build();
    }


    // 개인정보 변경
    public void updateUserInfo(Long userId, UserUpdateDto dto) {

//      주소, 비밀번호 힌트 전부 null인 경우, 정보를 변경하지 않는다.
        if (dto.getAddress() == null && dto.getPasswordHint() == null) {
            return;
        }

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

//      1. 주소만 변경하는 경우
        if (dto.getAddress() != null) {
            foundUser.setAddress(dto.getAddress());
        }

//      2. 비밀번호 힌트만 변경하는 경우
        if (dto.getPasswordHint() != null) {
            foundUser.setPasswordHint(dto.getPasswordHint());
        }

        userRepository.save(foundUser);
    }


    // 유저 삭제
    public void deleteUser(Long userId) {

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(foundUser);
    }


    // 계정 찾기
    public User findAccount(UserFindAccountDto dto) {

        if (!checkUser(dto)) {
            return null;
        }

        return userRepository.findByNameAndPhoneNumber(dto.getName(), dto.getPhoneNumber());
    }


    // 계정을 찾기 위한 본인 확인 메서드
    private Boolean checkUser(UserFindAccountDto dto) {
        return userRepository.existsByNameAndPhoneNumber(dto.getName(), dto.getPhoneNumber());
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return foundUser;
    }
}
