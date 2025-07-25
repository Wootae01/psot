package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
	@NotNull
	private Long parentCommentId;

	@NotBlank
	private String content;
}
