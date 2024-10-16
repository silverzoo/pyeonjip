package com.team5.pyeonjip.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDto {

    private String name;

    private String email;

    private String phoneNumber;

    private String address;
}
