package com.ulalala.board_jpa.service;

import com.ulalala.board_jpa.common.response.CommonResponse;
import com.ulalala.board_jpa.common.response.ResponseStatus;
import com.ulalala.board_jpa.common.response.SuccessMessage;
import com.ulalala.board_jpa.common.response.exception.ErrorCode;
import com.ulalala.board_jpa.common.response.exception.TicketingException;
import com.ulalala.board_jpa.dao.repository.UserRepository;
import com.ulalala.board_jpa.domain.User;
import com.ulalala.board_jpa.dto.user.UserLoginRequest;
import com.ulalala.board_jpa.dto.user.UserRequest;
import com.ulalala.board_jpa.dto.user.UserSession;
import com.ulalala.board_jpa.util.SessionUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public CommonResponse register(UserRequest userRequest) {

        if (userRepository.findUserById(userRequest.getId()) != null) {
            throw new TicketingException(ErrorCode.DUPLICATE_ID);
        } else {
            if (userRepository.findUserByEmail(userRequest.getEmail()) != null) {
                throw new TicketingException(ErrorCode.DUPLICATE_EMAIL);
            }
        }

        String hashPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
        userRequest.setPassword(hashPassword);

        User user = new User(userRequest);
        userRepository.save(user);
        return new CommonResponse(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_REGISTER.getMessage(), null);
    }

    public CommonResponse login(UserLoginRequest userLoginRequest) {

        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        if (userSession != null) {
            throw new TicketingException(ErrorCode.DUPLICATE_LOGIN);
        }

        // 아이디 확인
        User loginUser = userRepository.findUserById(userLoginRequest.getId());
        if (loginUser == null) {
            throw new TicketingException(ErrorCode.MISMATCH_ID);
        } else {
            if (!BCrypt.checkpw(userLoginRequest.getPassword(), loginUser.getPassword())){
                throw new TicketingException(ErrorCode.MISMATCH_PASSWORD);
            } else {
                if (createUserSession(loginUser) == null) {
                    throw new TicketingException(ErrorCode.FAIL_SESSION_CRATE);
                }
                return new CommonResponse(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_LOGIN.getMessage(), null);

            }
        }
    }

    public UserSession createUserSession(User loginUser) {
        UserSession userSession = new UserSession(loginUser.getIdx(), loginUser.getName());
        SessionUtils.setAttribute("USER", userSession);
        return userSession;
    }

    // 로그인한 유저 정보 가져오기
    public UserSession getLoginUserInfo() {
        return (UserSession) SessionUtils.getAttribute("USER");
    }

    public String logout() {
        SessionUtils.removeAttribute("USER");
        System.out.println("로그아웃");
        return SuccessMessage.SUCCESS_LOGOUT.getMessage();
    }

}
