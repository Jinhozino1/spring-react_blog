package com.jin.board_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jin.board_back.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByTelNumber(String telNumber);
    
    UserEntity findByEmail(String email);
}
