package com.team5.pyeonjip.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "password_hint", nullable = false)
    private String passwordHint;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private Grade grade;


    /* Mapping(ERD 참고 임시 작성) */

//    ChatRoom과 일대다 Optional 매핑
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ChatRoom> chatRooms;

//    Order와 일대다 Optional 매핑
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Order> orders;

//    Cart와 일대일 매핑
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false)
//    private Cart cart;


    // 회원가입 시 기본 권한을 "ROLE_USER", 등급을 "BRONZE"로 설정
    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = Role.ROLE_USER;
        }
        if (this.grade == null) {
            this.grade = Grade.BRONZE;
        }
    }

}
