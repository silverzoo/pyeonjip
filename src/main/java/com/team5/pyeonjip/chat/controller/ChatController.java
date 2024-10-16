package com.team5.pyeonjip.chat.controller;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.service.ChatMessageService;
import com.team5.pyeonjip.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // userId에 따른 채팅이력 리스트
    @GetMapping("/chat-room-list/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@PathVariable("userId") Long userId){
        List<ChatRoomDto> chatRoom = chatRoomService.getChatRoomsByUserId(userId);

        return ResponseEntity.ok().body(chatRoom);
    }

    // 채팅방 생성
    @PostMapping("/chat-room")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto){
        ChatRoomDto createdChatRoom = chatRoomService.createChatRoom(chatRoomDto);

        return ResponseEntity.ok().body(createdChatRoom);
    }

    // 채팅방에 따른 채팅 메시지 조회
    @GetMapping("/chat-message-history/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId){
        List<ChatMessageDto> chatMessage = chatMessageService.getChatMessagesByChatRoomId(chatRoomId);

        return ResponseEntity.ok().body(chatMessage);
    }
}
