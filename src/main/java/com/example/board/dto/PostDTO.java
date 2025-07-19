package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
	private String title;
	private String content;
	private Integer hits;
	private Integer likes;
}
