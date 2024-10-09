package com.team5.pyeonjip.chat.controller;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.entity.ChatMessage;
import com.team5.pyeonjip.chat.entity.ChatRoom;
import com.team5.pyeonjip.chat.service.ChatMessageService;
import com.team5.pyeonjip.chat.service.ChatRoomService;
import lombok.Getter;
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
    @GetMapping("/chat_room_List/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@PathVariable("userId") Long userId){
        List<ChatRoomDto> chatRoom = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok().body(chatRoom);
    }

    // 채팅방 생성
    @PostMapping("/chat_room")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody ChatRoomDto chatRoomDto){
        ChatRoomDto createdChatRoom = chatRoomService.createChatRoom(chatRoomDto);
        return ResponseEntity.ok().body(createdChatRoom);
    }


    // 메시지 보내기
    @PostMapping("/message")
    public ResponseEntity<ChatMessageDto> sendMessage( @RequestParam("chatRoomId") Long chatRoomId, @RequestBody String message){
        ChatMessageDto createdMessage = chatMessageService.sendMessage(chatRoomId, message);

        return ResponseEntity.ok().body(createdMessage);
    }

    // 메시지 수정
    @PutMapping("/message/{messageId}")
    public ResponseEntity<ChatMessageDto> modifyMessage(@PathVariable("messageId") Long messageId, @RequestBody String message){
        ChatMessageDto modifiedMessage = chatMessageService.modifyMessage(messageId, message);
        System.out.println(modifiedMessage.toString());
        return ResponseEntity.ok().body(modifiedMessage);
    }
}
