package com.jin.board_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jin.board_back.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{
    List<ImageEntity> findByBoardNumber(Integer boardNumber);
}
