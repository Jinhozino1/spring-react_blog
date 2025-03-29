package com.jin.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jin.board_back.entity.CommentEntity;


@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{
    
}
