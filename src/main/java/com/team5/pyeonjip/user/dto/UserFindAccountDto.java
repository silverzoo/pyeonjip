package com.team5.pyeonjip.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserFindAccountDto {

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;
}
