package com.team5.pyeonjip.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 한 유저가 여러 토큰을 발급할 수 있으므로 not unique
    @Column(nullable = false)
    private String email;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private String refresh;

    @Column(nullable = false)
    private String expiration;
}
