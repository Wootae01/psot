package com.example.board.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
	private Long id;
	private String username;
	private String nickname;
	private String title;
	private String content;
	private Integer hits;
	private Integer likes;
	private LocalDateTime createDate;

}
