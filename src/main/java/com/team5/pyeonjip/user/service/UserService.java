package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.mapper.UserMapper;
import com.team5.pyeonjip.user.dto.UserUpdateDto;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /* CREATE */

    public void createUser(SignUpDto dto) {
        User user = UserMapper.INSTANCE.toEntity(dto);

        userRepository.save(user);
    }


    /* UPDATE */

    public void updateUserInfo(Long id, UserUpdateDto dto) {

//      주소, 비밀번호 힌트 전부 null인 경우, 정보를 변경하지 않는다.
        if (dto.getAddress() == null && dto.getPwHint() == null) {
            throw new IllegalArgumentException("변경할 정보가 없습니다.");
        }

        User findedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

//      1. 주소만 변경하는 경우
        if (dto.getAddress() != null) {
            findedUser.setAddress(dto.getAddress());
        }

//      2. 비밀번호 힌트만 변경하는 경우
        if (dto.getPwHint() != null) {
            findedUser.setPwHint(dto.getPwHint());
        }

        userRepository.save(findedUser);
    }


    /* READ */

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(Long id) {
        User findedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return findedUser;
    }


    /* DELETE */

    public void deleteUser(Long id) {

        User findedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(findedUser);
    }
}
