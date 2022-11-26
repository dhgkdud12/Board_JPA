package com.ulalala.board_jpa.dto.board;

import com.ulalala.board_jpa.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class BoardDto {

    private Integer boardNo;
    private String title;
    private String content;
    private Integer userIdx;
    private Timestamp createDate;
    private Timestamp updateDate;
    private List<Comment> comments;

}