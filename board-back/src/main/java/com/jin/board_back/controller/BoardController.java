package com.jin.board_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jin.board_back.dto.request.board.PostBoardRequestDto;
import com.jin.board_back.dto.response.board.PostBoardResponseDto;
import com.jin.board_back.dto.response.board.GetBoardResponseDto;
import com.jin.board_back.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

   //  @PostMapping("")
   //  public ResponseEntity<? super PostBoardResponseDto> postBoard (
   //      @RequestBody @Valid PostBoardRequestDto requestDto,
   //      @AuthenticationPrincipal String email
   //   ) {
   //      ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard((requestDto), email);
   //      return response;
   //   }


   @GetMapping("/{boardNumber}")
   public ResponseEntity<? super  GetBoardResponseDto> getBoard (
      @PathVariable("boardNumber") Integer boardNumber 
   ) {
      ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoard(boardNumber);
      return response;
      
   }

   @PostMapping("")
   public ResponseEntity<? super PostBoardResponseDto> postBoard (
    @RequestBody @Valid PostBoardRequestDto requestDto
   ) {
      // ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard((requestDto), email);
      String email = SecurityContextHolder.getContext().getAuthentication().getName();
      // System.out.println("✅ 인증된 사용자: " + email);
      return boardService.postBoard(requestDto, email);
   }
    
}
