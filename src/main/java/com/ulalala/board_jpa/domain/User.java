package com.ulalala.board_jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ulalala.board_jpa.dto.user.UserRequest;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 사용자
public class User {
    private Integer idx;
    private String id;
    private String name;
    private String password;
    private String email;
    private Timestamp joinDate;

    public User(UserRequest userRequest) {
        this.id = userRequest.getId();
        this.name = userRequest.getName();
        this.password = userRequest.getPassword();
        this.password = userRequest.getPassword();
        this.email = userRequest.getEmail();
        this.joinDate = new Timestamp(new Date().getTime());
    }
}
