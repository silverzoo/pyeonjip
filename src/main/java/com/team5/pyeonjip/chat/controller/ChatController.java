package com.team5.pyeonjip.chat.controller;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.entity.ChatMessage;
import com.team5.pyeonjip.chat.entity.ChatRoom;
import com.team5.pyeonjip.chat.service.ChatMessageService;
import com.team5.pyeonjip.chat.service.ChatRoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    @GetMapping("/chat_room_list/{userId}")
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

    // 채팅방에 따른 채팅 메시지 조회
    @GetMapping("/chat_message_history/{chatRoomId}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId){
        List<ChatMessageDto> chatMessage = chatMessageService.getChatMessagesByChatRoomId(chatRoomId);

        return ResponseEntity.ok().body(chatMessage);
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

        return ResponseEntity.ok().body(modifiedMessage);
    }

    // 메시지 삭제
    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") Long messageId){
        chatMessageService.deleteMessage(messageId);

        return ResponseEntity.ok().build();
    }
}
