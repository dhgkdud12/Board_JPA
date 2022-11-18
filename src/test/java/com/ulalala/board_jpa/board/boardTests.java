package com.ulalala.board_jpa.board;

import com.ulalala.board_jpa.dao.repository.BoardRepository;
import com.ulalala.board_jpa.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class boardTests {

    @Autowired
    BoardRepository boardRepository;

    @DisplayName("게시물 작성 테스트")
    @Test
    void save() {

        // given(준비)
        // when(실행)
        // then(검증)

        // 1. 게시글 파라미터 생성
        Board params = Board.builder()
                .title("1번 게시글 제목")
                .content("1번 게시글 내용")
                .userIdx(1)
                .createDate(new Timestamp(new Date().getTime()))
                .updateDate(null)
                .build();

        // 2. 게시글 저장
        System.out.println("저장: " + boardRepository.save(params).getClass());

        // 3. 게시글 정보 조회
        Board entity = boardRepository.findById(1).get();
        assertThat(entity.getTitle()).isEqualTo("제목");
        assertThat(entity.getContent()).isEqualTo("내용");
        assertThat(entity.getUserIdx()).isEqualTo(1);
    }

    @Test
    void findAll() {
        // 1. 전체 게시글 수 조회
        long boardsCount = boardRepository.count();

        // 2. 전체 게시글 리스트 조회
        List<Board> boards = boardRepository.findAll();
    }
}
