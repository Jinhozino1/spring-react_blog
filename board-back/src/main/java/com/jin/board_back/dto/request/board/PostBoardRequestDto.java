package com.jin.board_back.dto.request.board;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostBoardRequestDto {
    
    @NotBlank
    private String title;

    @NotBlank
    private String content;
    
    @NotEmpty
    private List<String> boardImageList;
}
