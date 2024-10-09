package com.team5.pyeonjip.chat.mapper;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    // Entity -> DTO
    @Mapping(source = "chatRoomId", target = "chatRoomId")
    ChatMessageDto toDTO(ChatMessage chatMessage);
}
