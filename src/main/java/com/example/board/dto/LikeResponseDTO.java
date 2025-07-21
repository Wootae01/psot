package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponseDTO {
    private boolean hasLiked;
    private Integer likeCount;
}
