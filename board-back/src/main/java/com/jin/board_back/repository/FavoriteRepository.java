package com.jin.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jin.board_back.entity.FavoriteEntity;
import com.jin.board_back.entity.primaryKey.FavoritePk;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk>{
    
}
