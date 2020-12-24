package com.tamastudy.jon.repository;

import com.tamastudy.jon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // findBy규칙 -> Username 문법
    // select * from user where username = ?
    public User findByUsername(String username); // JPA Query methods

    // select * from user where email = ?
    public User findByEmail(String email);
}
