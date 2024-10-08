package com.team5.pyeonjip.chat.controller;

import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.entity.ChatRoom;
import com.team5.pyeonjip.chat.service.ChatMessageService;
import com.team5.pyeonjip.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/chat_room/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@PathVariable("userId") Long userId){
        List<ChatRoomDto> chatRoom = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok().body(chatRoom);
    }

    // 채팅방 생성
    @PostMapping("/chat_room")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto){
        ChatRoomDto createChatRoom = chatRoomService.createChatRoom(chatRoomDto);
        return ResponseEntity.ok().body(createChatRoom);
    }

    // 메시지 보내기
    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(@RequestBody String message, @RequestParam("chatRoomId") Long chatRoomId){
        chatMessageService.sendMessage(chatRoomId, message);
        return ResponseEntity.ok().build();
    }
}
