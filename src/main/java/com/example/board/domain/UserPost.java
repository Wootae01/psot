package com.example.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPost {

	@Id
	@Column(name = "user_post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;

	@JoinColumn(name = "post_id")
	@ManyToOne
	private Post post;

	@Enumerated(EnumType.STRING)
	private ActionType type;

	public UserPost(User user, Post post, ActionType actionType) {
		this.user = user;
		this.post = post;
		this.type = actionType;
	}
}
