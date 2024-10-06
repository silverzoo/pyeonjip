package com.team5.pyeonjip.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private Long id;
    private String category;
    private boolean isClosed;
    private Timestamp closedAt;
    private Long userId;
    private Long adminId;
}
