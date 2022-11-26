package com.ulalala.board_jpa.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentNo;

    @Column(nullable = false)
    private Integer boardNo;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer userIdx;

    @Column(nullable = false)
    private Timestamp date;

    // 대댓글

    // 부모 댓글 ID
    @Column(nullable = false)
    private Integer parentId;

    // 그룹 내 번호 - 댓글 1, 댓글 2, 댓글 3
    @Column(nullable = false)
    private Integer groupNo;

    // 댓글 계층 댓글, 대댓글, 대댓글의 댓글
    @Column(nullable = false)
    private Integer layer;

    // 자식 댓글 수
    @Column(nullable = false)
    private Integer childCnt;

    // 댓글 그룹들의 순서 -
    @Column(nullable = false)
    private Integer groupOrd;

    @Builder
    public Comment(Integer commentNo, Integer boardNo, String content, Integer userIdx, Timestamp date, Integer parentId, Integer groupNo, Integer layer, Integer childCnt, Integer groupOrd) {
        this.commentNo = commentNo;
        this.boardNo = boardNo;
        this.content = content;
        this.userIdx = userIdx;
        this.date = date;
        this.parentId = parentId;
        this.groupNo = groupNo;
        this.layer = layer;
        this.childCnt = childCnt;
        this.groupOrd = groupOrd;
    }
}
