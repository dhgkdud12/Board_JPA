package com.ulalala.board_jpa.controller;

import com.ulalala.board_jpa.common.response.ResponseStatus;
import com.ulalala.board_jpa.dto.user.UserLoginRequest;
import com.ulalala.board_jpa.dto.user.UserRequest;
import com.ulalala.board_jpa.common.response.CommonResponse;
import com.ulalala.board_jpa.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("")
    public CommonResponse register(@Valid @RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    // 로그인
    @PostMapping ("/login")
    public CommonResponse logIn(@RequestBody UserLoginRequest userLoginRequest) {
        return userService.login(userLoginRequest);
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public CommonResponse logout() {
        String message = userService.logout();
        return new CommonResponse(ResponseStatus.SUCCESS, 200, message, null);
    }
}
