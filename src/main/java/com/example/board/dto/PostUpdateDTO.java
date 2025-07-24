package com.example.board.dto;

import com.example.board.validator.NotEmptyContent;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateDTO {

	@NotBlank
	private String title;

	@NotEmptyContent
	private String content;
}
