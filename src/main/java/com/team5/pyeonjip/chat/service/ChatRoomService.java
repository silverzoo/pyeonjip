package com.team5.pyeonjip.chat.service;

import com.team5.pyeonjip.chat.dto.ChatRoomDto;
import com.team5.pyeonjip.chat.entity.ChatRoom;
import com.team5.pyeonjip.chat.mapper.ChatRoomMapper;
import com.team5.pyeonjip.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    public List<ChatRoomDto> getChatRooms(){
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDtos.add(chatRoomMapper.toDTO(chatRoom));
        }
        return chatRoomDtos;
    }

    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto){
        ChatRoom chatRoom = ChatRoom.builder()
                .category(chatRoomDto.getCategory())
                .isClosed(chatRoomDto.isClosed())
                .userId(1L)
                .adminId(2L)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDTO(savedChatRoom);
    }

    public List<ChatRoomDto> getChatRoomsByUserId(Long userId){
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);

        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomDtos.add(chatRoomMapper.toDTO(chatRoom));
        }
        return chatRoomDtos;
    }
}
