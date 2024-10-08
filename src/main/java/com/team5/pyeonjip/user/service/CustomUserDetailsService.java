package com.team5.pyeonjip.user.service;

import com.team5.pyeonjip.user.dto.CustomUserDetails;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    // email을 매개변수로 가지도록 재정의
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User userData = userRepository.findByEmail(email);

        // 데이터 유효성 검증
        if (userData != null) {

            // AuthenticationManager에게 userData를 넘겨준다.
            return new CustomUserDetails(userData);
        }


        return null;
    }
}
