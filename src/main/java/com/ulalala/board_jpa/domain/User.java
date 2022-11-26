package com.ulalala.board_jpa.domain;

import lombok.*;
import com.ulalala.board_jpa.dto.user.UserRequest;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
// 사용자
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Timestamp joinDate;


    @Builder
    public User(UserRequest userRequest) {
        this.id = userRequest.getId();
        this.name = userRequest.getName();
        this.password = userRequest.getPassword();
        this.password = userRequest.getPassword();
        this.email = userRequest.getEmail();
        this.joinDate = new Timestamp(new Date().getTime());
    }
}
