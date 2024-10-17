package com.team5.pyeonjip.chat.entity;

import com.team5.pyeonjip.global.entity.BaseTimeEntity;
import com.team5.pyeonjip.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "is_closed")
    private boolean isClosed = false;

    @Column(name = "closed_at")
    private Timestamp closedAt;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;


//    @ManyToOne
//    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "admin_id", referencedColumnName = "id", nullable = false)
//    private User admin;


    // 채팅방 종료 상태 업데이트
    public void closeChatRoom(){
        this.isClosed = true;
    }

    @Builder
    public ChatRoom(String category, boolean isClosed, Long userId, Long adminId){
        this.category = category;
        this.isClosed = isClosed;
        this.userId = userId;
        this.adminId = adminId;
    }
}
