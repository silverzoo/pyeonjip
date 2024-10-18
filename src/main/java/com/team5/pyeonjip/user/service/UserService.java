package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
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


    public boolean signUpProcess(SignUpDto dto) {

//      1. 중복 이메일 검증
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

//      2. 중복 이메일이 없으면 회원가입 절차 실행
        try {
//          2 - 1. 비밀번호 인코딩
            String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());

//          2 - 2. dto 엔티티화
            User user = UserMapper.INSTANCE.toEntity(dto);

//          2 - 3. user 객체에 인코딩된 비밀번호 값을 설정
            user.setPassword(encodedPassword);

            userRepository.save(user);

            return true;
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.USER_SIGNUP_FAILED);
        }


    }


    // 마이페이지 조회
    public UserInfoDto getUserInfo(String email) {

        // 유저를 찾지 못할 경우 예외처리.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        return UserInfoDto.builder().name(user.getName())
                                    .email(user.getEmail())
                                    .phoneNumber(user.getPhoneNumber())
                                    .address(user.getAddress())
                                    .build();
    }


    // 개인정보 변경
    public void updateUserInfo(String email, UserUpdateDto dto) {

//      주소, 비밀번호 힌트 전부 null인 경우, 정보를 변경하지 않는다.
        // Todo: DynamicUpdate 적용하기
        if (dto.getAddress() == null && dto.getPasswordHint() == null) {
            throw new GlobalException(ErrorCode.INVALID_USER_UPDATE);
        }

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

//      1. 주소만 변경하는 경우
        if (dto.getAddress() != null) {
            foundUser.setAddress(dto.getAddress());
        }

//      2. 비밀번호 힌트만 변경하는 경우
        if (dto.getPasswordHint() != null) {
            foundUser.setPasswordHint(dto.getPasswordHint());
        }

        // Todo: 마찬가지로 DynamicUpdate 사용 시 필요 없을수도.
        userRepository.save(foundUser);
    }


    // 유저 삭제
    public void deleteUser(String email) {

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException((ErrorCode.ACCOUNT_NOT_FOUND)));

        try {
            userRepository.delete(foundUser);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.USER_DELETE_FAILED);
        }

    }


    // 계정 찾기
    public User findAccount(UserFindAccountDto dto) {

        if (!checkUser(dto)) {
            return null;
        }

        return userRepository.findByNameAndPhoneNumber(dto.getName(), dto.getPhoneNumber())
                .orElseThrow(() -> new GlobalException(ErrorCode.ACCOUNT_NOT_FOUND));
    }


    // 계정을 찾기 위한 본인 확인 메서드
    private Boolean checkUser(UserFindAccountDto dto) {

        return userRepository.existsByNameAndPhoneNumber(dto.getName(), dto.getPhoneNumber());
    }


    public List<User> findAllUsers() {

        return userRepository.findAll();
    }

    public User findUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }


    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
