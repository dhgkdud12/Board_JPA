package com.ulalala.board_jpa.service;

import com.ulalala.board_jpa.dao.repository.BoardRepository;
import com.ulalala.board_jpa.domain.Board;
import org.springframework.stereotype.Service;
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void post(Board board) {
        boardRepository.save(board);
    }

    // 페이지
    public void selectAllPosts() {
    }


    public void selectPostByPostId(Integer bIdx) {
    }

    // 내 게시물
    public void selectPostsByUserId() {
    }

    public void updatePost(int bIdx) {

    }

    public void deletePost(int bIdx) {
    }
}
