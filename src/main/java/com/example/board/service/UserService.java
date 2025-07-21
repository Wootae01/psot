package com.example.board.service;

import com.example.board.domain.ActionType;
import com.example.board.domain.UserPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.User;
import com.example.board.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public boolean hasLiked(User user, Long postId) {
		List<UserPost> userPosts = user.getUserPosts();
		for (UserPost userPost : userPosts) {
			if (userPost.getType().equals(ActionType.LIKE) && userPost.getPost().getId().equals(postId)) {
				return true;
			}
		}
		return false;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
