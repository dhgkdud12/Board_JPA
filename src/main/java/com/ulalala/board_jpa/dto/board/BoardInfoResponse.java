package com.ulalala.board_jpa.dto.board;

import com.ulalala.board_jpa.domain.Board;
import com.ulalala.board_jpa.dto.comment.CommentDto;
import com.ulalala.board_jpa.dto.file.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardInfoResponse {
    private Board board;
    private FileResponse fileResponse;
    private List<CommentDto> commentDtos;
}

