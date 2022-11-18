package com.ulalala.board_jpa.controller;

import com.ulalala.board_jpa.common.response.ResponseStatus;
import com.ulalala.board_jpa.dto.board.BoardInfoResponse;
import com.ulalala.board_jpa.dto.board.BoardRequest;
import com.ulalala.board_jpa.dto.comment.CommentDto;
import com.ulalala.board_jpa.dto.comment.CommentRequest;
import com.ulalala.board_jpa.dto.user.UserSession;
import com.ulalala.board_jpa.common.response.CommonResponse;
import com.ulalala.board_jpa.common.response.SuccessMessage;
import com.ulalala.board_jpa.service.BoardService;
import com.ulalala.board_jpa.service.CommentService;
import com.ulalala.board_jpa.util.SessionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    public CommonResponse home(
            @RequestParam(name = "page", required = false, defaultValue = "1") int curPage,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "blockSize", required = false, defaultValue = "5") int blockSize,
            @RequestParam(name = "searchType", required = false, defaultValue = "title") String searchType,
            @RequestParam(name = "keyword", required = false) String keyword) {
        Map<String, Object> resultMap = new HashMap<>();
        UserSession userSession = (UserSession) SessionUtils.getAttribute("USER");
        if (userSession != null) {
            System.out.println(userSession.getName()+"님 로그인중");
        }
        
        resultMap.put("boardInfo", boardService.selectAllPosts(searchType, keyword, curPage, size, blockSize));
        resultMap.put("pageInfo", boardService.getPagingInfo(curPage, size, blockSize));

        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), resultMap);
    }


    // 게시물 작성
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse post(@Valid BoardRequest boardRequest, HttpServletRequest request) {
        return boardService.post(boardRequest, request);
    }

    @GetMapping("/{bIdx}")
    public CommonResponse selectPost(@PathVariable("bIdx") Integer bIdx) {
        BoardInfoResponse boardInfo = boardService.selectPostByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), boardInfo);

    }
    
    // 게시물 수정
    // 내가 작성한 게시물만 수정 가능
    @PutMapping("/{bIdx}")
    public CommonResponse updatePost(@PathVariable("bIdx") int bIdx, @Valid @RequestBody BoardRequest boardRequest) {
        return boardService.updatePost(bIdx, boardRequest);
    }
    
    // 게시물 삭제
    // 내가 삭제한 게시물만 삭제 가능
    @DeleteMapping("/{bIdx}")
    public CommonResponse deletePost(@PathVariable("bIdx") int bIdx) {
        return boardService.deletePost(bIdx);
    }

    // 댓글 작성
    @PostMapping("/{bIdx}")
    public CommonResponse postComment(@PathVariable("bIdx") int bIdx, @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.post(bIdx, commentRequest);
    }

    @GetMapping("/{bIdx}/comment")
    public CommonResponse selectComment(@PathVariable("bIdx") int bIdx) {
        List<CommentDto> comments = commentService.selectCommentsByPostId(bIdx);
        return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_READ.getMessage(), comments);
    }

    @DeleteMapping("/{bIdx}/comment/{cIdx}")
    public CommonResponse deleteComment(@PathVariable("bIdx") int bIdx, @PathVariable("cIdx") int cIdx) {
        return commentService.delete(cIdx);
    }
}
