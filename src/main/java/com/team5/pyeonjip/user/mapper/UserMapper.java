package com.team5.pyeonjip.user.mapper;

import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "grade", ignore = true)
    User toEntity(SignUpDto dto);
}