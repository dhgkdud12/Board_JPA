package com.ulalala.board_jpa.controller;

import com.ulalala.board_jpa.domain.Board;
import com.ulalala.board_jpa.dto.user.UserSession;
import com.ulalala.board_jpa.service.BoardService;
import com.ulalala.board_jpa.service.CommentService;
import com.ulalala.board_jpa.util.SessionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    // 홈화면
    @GetMapping("")
    public void home() {
        Map<String, Object> resultMap = new HashMap<>();
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        if (userSession != null) {
            System.out.println(userSession.getName()+"님 로그인중");
        }

    }


    // 게시물 작성
//    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping()
    public void post(@RequestBody Board board) {
        System.out.println(board.toString());
        boardService.post(board);
    }

    @GetMapping("/{bIdx}")
    public void selectPost(@PathVariable("bIdx") Integer bIdx) {
    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public void updatePost() {
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public void deletePost(@PathVariable("bIdx") int bIdx) {
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public void postComment(@PathVariable("bIdx") int bIdx) {
    }

    @GetMapping("/{bIdx}/comment")
    public void selectComment(@PathVariable("bIdx") int bIdx) {
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public void deleteComment(@PathVariable("bIdx") int bIdx, @PathVariable("cIdx") int cIdx) {
    }
}
