package com.tamastudy.jon.repository;

import com.tamastudy.jon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
