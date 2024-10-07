package com.team5.pyeonjip.chat.service;

import com.team5.pyeonjip.chat.entity.ChatMessage;
import com.team5.pyeonjip.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(Long chatRoomId, String message){
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .message(message)
                .senderEmail("abc@naver.com")
                .build();

        chatMessageRepository.save(chatMessage);
    }
}
