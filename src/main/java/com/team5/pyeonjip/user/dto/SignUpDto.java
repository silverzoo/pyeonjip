package com.team5.pyeonjip.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String phoneNum;

    @NotNull
    private String password;

    @NotNull
    private String address;

    @NotNull
    private String pwHint;
}
