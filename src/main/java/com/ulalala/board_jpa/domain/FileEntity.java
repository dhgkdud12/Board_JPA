package com.ulalala.board_jpa.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileNo;
    private Integer boardNo;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String convertName;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Builder
    public FileEntity(Integer fileNo, Integer boardNo, String fileName, String convertName, String path, String extension, Long size) {
        this.fileNo = fileNo;
        this.boardNo = boardNo;
        this.fileName = fileName;
        this.convertName = convertName;
        this.path = path;
        this.extension = extension;
        this.size = size;
    }
}
