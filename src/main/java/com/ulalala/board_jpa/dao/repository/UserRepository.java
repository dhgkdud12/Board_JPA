package com.ulalala.board_jpa.dao.repository;

import com.ulalala.board_jpa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(String id);
    User findUserByEmail(String email);
}
