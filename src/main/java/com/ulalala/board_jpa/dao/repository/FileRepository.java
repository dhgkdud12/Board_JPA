package com.ulalala.board_jpa.dao.repository;

import com.ulalala.board_jpa.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    FileEntity findFileByBoardNo(Integer boardNo);
}
