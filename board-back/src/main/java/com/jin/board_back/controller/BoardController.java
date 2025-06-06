package com.jin.board_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.azul.crs.client.Response;
import com.jin.board_back.config.CustomUserDetails;
import com.jin.board_back.dto.request.board.PatchBoardRequestDto;
import com.jin.board_back.dto.request.board.PostBoardRequestDto;
import com.jin.board_back.dto.request.board.PostCommentRequestDto;
import com.jin.board_back.dto.response.board.PostBoardResponseDto;
import com.jin.board_back.dto.response.board.PutFavoriteResponseDto;
import com.jin.board_back.dto.response.board.GetBoardResponseDto;
import com.jin.board_back.dto.response.board.GetFavoriteListResponseDto;
import com.jin.board_back.dto.response.board.*;
import com.jin.board_back.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // ----------------  GET  ---------------
   @GetMapping("/{boardNumber}")
   public ResponseEntity<? super  GetBoardResponseDto> getBoard (
      @PathVariable("boardNumber") Integer boardNumber 
   ) {
      ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoard(boardNumber);
      return response;
      
   }

   @GetMapping("/{boardNumber}/favorite-list")
   public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList (
      @PathVariable("boardNumber") Integer boardNumber
   ) {
      ResponseEntity<? super GetFavoriteListResponseDto> response = boardService.getFavoriteList(boardNumber);
      return response;
   }

   @GetMapping("/{boardNumber}/comment-list")
   public ResponseEntity<? super GetCommentListResponseDto> getCommentList (
      @PathVariable("boardNumber") Integer boardNumber
   ) {
      ResponseEntity<? super GetCommentListResponseDto> response = boardService.getCommentList(boardNumber);
      return response;
   }

   @GetMapping("/{boardNumber}/increse-view-count")
   public ResponseEntity<? super IncreseViewCountResponseDto> increseViewCount (
      @PathVariable("boardNumber") Integer boardNumber
   ) {
      ResponseEntity<? super IncreseViewCountResponseDto> response = boardService.increseViewCount(boardNumber);
      return response;
   }

   @GetMapping("/latest-list")
   public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {
      ResponseEntity<? super GetLatestBoardListResponseDto> response = boardService.getLatesBoardList();
      return response;
   }

   @GetMapping("/top-3")
   public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {
      ResponseEntity<? super GetTop3BoardListResponseDto> response = boardService.getTop3BoardList();
      return response;
   }

   @GetMapping(value = {"/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}"})
   public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList (
      @PathVariable("searchWord") String searchWord,
      @PathVariable(value = "preSearchWord", required = false) String preSearchWord
   ) {
      ResponseEntity<? super GetSearchBoardListResponseDto> response = boardService.getSearchBoardList(searchWord, preSearchWord);
      return response;
}

   @GetMapping("/user-board-list/{email}")
   public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList (
      @PathVariable("email") String email
   ) {
      ResponseEntity<? super GetUserBoardListResponseDto> response = boardService.getUserBoardList(email);
      return response;
   }



//---------------  POST  ---------------

    @PostMapping("")
    public ResponseEntity<? super PostBoardResponseDto> postBoard (
        @RequestBody @Valid PostBoardRequestDto requestDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
     ) {
         String email = userDetails.getEmail();
         ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard((requestDto), email);
         return response;
      }

   @PostMapping("/{boardNumber}/comment")
   public ResponseEntity<? super PostCommentResponseDto> postComment(
      @RequestBody @Valid PostCommentRequestDto requestBody,
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal CustomUserDetails userDetails
   ) {
      String email = userDetails.getEmail();
      ResponseEntity<? super PostCommentResponseDto> response = boardService.postComment(requestBody, boardNumber, email);
      return response;
   }
    
   @PutMapping("/{boardNumber}/favorite")
   public ResponseEntity<? super PutFavoriteResponseDto> putFavorite (
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal CustomUserDetails userDetails
   ) {
      String email = userDetails.getEmail();
      ResponseEntity<? super PutFavoriteResponseDto> response = boardService.putFavorite(boardNumber, email);
      return response;
   }

   @PatchMapping("/{boardNumber}")
   public ResponseEntity<? super PatchBoardResponseDto> patchBoard (
      @RequestBody @Valid PatchBoardRequestDto requestBody,
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal CustomUserDetails userDetails
   ) {
      String email = userDetails.getEmail();
      ResponseEntity<? super PatchBoardResponseDto> response = boardService.patchBoard(requestBody, boardNumber, email);
      return response;
   }

   @DeleteMapping("/{boardNumber}")
   public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard (
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal CustomUserDetails userDetails 
   ) {
      String email = userDetails.getEmail();
      ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(boardNumber, email);
      return response;
   }
   
}
