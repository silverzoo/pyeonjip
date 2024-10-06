package com.team5.pyeonjip.user.dto;

import com.team5.pyeonjip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                // 리턴타입 String에 맞추기 위해, name() 메서드를 사용한다.
                return user.getRole().name();
            }
        });

        return collection;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    // 이메일로 로그인하려는 의도에 맞게 getEmail() 메서드를 사용.
    // 오버라이드해야 되기 때문에 getUsername 메서드명을 사용.
    @Override
    public String getUsername() {
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
