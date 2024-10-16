package com.team5.pyeonjip.chat.controller;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    public void sendMessage(@DestinationVariable("chatRoomId") Long chatRoomId, @Payload ChatMessageDto message) {
        ChatMessageDto createdMessage = chatMessageService.sendMessage(chatRoomId, message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages/" + chatRoomId, createdMessage);
    }

    @MessageMapping("/chat.updateMessage/{chatRoomId}")
    public void updateMessage(@DestinationVariable("chatRoomId") Long chatRoomId, @Payload ChatMessageDto message) {
        ChatMessageDto updatedMessage = chatMessageService.updateMessage(message.getId(), message.getMessage());
        messagingTemplate.convertAndSend("/topic/message-updates/" + chatRoomId, updatedMessage);
    }

    @MessageMapping("/chat.deleteMessage/{chatRoomId}")
    public void deleteMessage(@DestinationVariable("chatRoomId") Long chatRoomId, @Payload ChatMessageDto message) {
        Long deletedMessageId = chatMessageService.deleteMessage(message.getId());
        messagingTemplate.convertAndSend("/topic/message-deletions/" + chatRoomId, deletedMessageId);
    }
}
