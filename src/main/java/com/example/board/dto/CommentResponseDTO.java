package com.example.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {
	private Long id;
	private String nickname;
	private String content;
	private List<CommentResponseDTO> replies = new ArrayList<>();
	private LocalDateTime createDate;

	public void addReplies(CommentResponseDTO reply) {
		replies.add(reply);
	}
}
