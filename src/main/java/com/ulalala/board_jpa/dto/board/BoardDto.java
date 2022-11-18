package com.ulalala.board_jpa.dto.board;

import com.ulalala.board_jpa.domain.Board;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class BoardDto {

    private Integer boardNo;

    private String title;

    private String content;

    private Integer userIdx;

    private Timestamp createDate;

    private Timestamp updateDate;

    public BoardDto(Board board) {
        this.boardNo = board.getBoardNo();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userIdx = board.getUserIdx();
        this.createDate = board.getCreateDate();
        this.updateDate = board.getUpdateDate();
    }
}