package com.jin.board_back.service;

import org.springframework.http.ResponseEntity;

import com.jin.board_back.dto.response.search.*;;

public interface SearchService {

    ResponseEntity<? super GetPopularListResponseDto> getPopularList();
    ResponseEntity<? super GetRelationListResponseDto> getRelationList(String searchWord);
} 
