package com.team5.pyeonjip.user.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UserUpdateDto {

    private String address;

    private String pwHint;
}
