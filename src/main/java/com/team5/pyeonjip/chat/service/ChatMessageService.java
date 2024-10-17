package com.team5.pyeonjip.chat.service;

import com.team5.pyeonjip.chat.dto.ChatMessageDto;
import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.entity.ChatMessage;
import com.team5.pyeonjip.chat.mapper.ChatMessageMapper;
import com.team5.pyeonjip.chat.mapper.ChatRoomMapper;
import com.team5.pyeonjip.chat.repository.ChatMessageRepository;
import com.team5.pyeonjip.global.exception.ErrorCode;
import com.team5.pyeonjip.global.exception.GlobalException;
import com.team5.pyeonjip.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    public List<ChatMessageDto> getChatMessagesByChatRoomId(Long chatRoomId){
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessages) {
            chatMessageDtos.add(chatMessageMapper.toDTO(chatMessage));
        }

        return chatMessageDtos;
    }

    public ChatMessageDto sendMessage(Long chatRoomId, String message){

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .message(message)
                .senderEmail("abc@naver.com")
                .build();

        chatMessage = chatMessageRepository.save(chatMessage);

        return chatMessageMapper.toDTO(chatMessage);
    }

    @Transactional
    public ChatMessageDto updateMessage(Long messageId, String message){
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.error("Message not found with ID: {}", messageId);
                    return new GlobalException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
                });

        chatMessage.updateMessage(message);

        chatMessageRepository.save(chatMessage);

        return chatMessageMapper.toDTO(chatMessage);
    }

    @Transactional
    public Long deleteMessage(Long messageId){
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.error("Message not found with ID: {}", messageId);
                    return new GlobalException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
                });

        chatMessageRepository.delete(chatMessage);

        return messageId;
    }
}
