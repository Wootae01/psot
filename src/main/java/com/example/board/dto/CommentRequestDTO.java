package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
	private Long parentCommentId;
	private String content;
}
