package com.jin.board_back.service;

import org.springframework.http.ResponseEntity;

import com.jin.board_back.dto.request.board.PatchBoardRequestDto;
import com.jin.board_back.dto.request.board.PostBoardRequestDto;
import com.jin.board_back.dto.request.board.PostCommentRequestDto;
import com.jin.board_back.dto.response.board.*;


public interface BoardService {
    ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);
    ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber);
    ResponseEntity<? super GetCommentListResponseDto> getCommentList (Integer boardNumber);
    ResponseEntity<? super GetLatestBoardListResponseDto> getLatesBoardList ();
    ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList();
    ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord);
    ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email);
    ResponseEntity<? super PostBoardResponseDto> postBoard (PostBoardRequestDto dto, String email);
    ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email);
    ResponseEntity<? super PutFavoriteResponseDto> putFavorite (Integer boardNumber, String email) ;
    ResponseEntity<? super PatchBoardResponseDto> patchBoard (PatchBoardRequestDto dto, Integer boardNumber, String email);
    ResponseEntity<? super IncreseViewCountResponseDto> increseViewCount(Integer boardNumber);
    ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email);
}
