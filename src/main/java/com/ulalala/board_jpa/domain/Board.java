package com.ulalala.board_jpa.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가
    private Integer boardNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer userIdx;

    @Column(nullable = false)
    private Timestamp createDate;

    @Column(nullable = false)
    private Timestamp updateDate;

    @OneToMany
    @JoinColumn(name = "boardNo")
    private List<Comment> comments;

    @Builder // 생성자에 @Builder 적용
    public Board(Integer boardNo, String title, String content, Integer userIdx, Timestamp createDate, Timestamp updateDate) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.userIdx = userIdx;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

//    public void edit(BoardEditor boardEditor) {
//        title = boardEditor.getTitle();
//        content = boardEditor.getContent();
//        updateDate = new Timestamp(new Date().getTime());
//    }
}