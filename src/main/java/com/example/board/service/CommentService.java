package com.example.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.dto.CommentResponseDTO;
import com.example.board.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	public Comment save(User user, Post post, Comment parent, String content, Integer likes) {
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setContent(content);
		comment.setPost(post);
		comment.setLikes(likes);
		comment.setParent(parent);
		return commentRepository.save(comment);
	}

	public Comment findById(Long id) {
		return commentRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("comment not found"));
	}

	public List<Comment> findRootComments(Long postId) {
		return commentRepository.findRootComments(postId);
	}

	public CommentResponseDTO convertToCommentResponseDTO(Comment comment) {
		CommentResponseDTO dto = new CommentResponseDTO();

		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		dto.setNickname(comment.getUser().getNickname());
		dto.setCreateDate(comment.getCreateDate());
		List<Comment> children = comment.getChildren();

		for (Comment child : children) {
			dto.addReplies(convertToCommentResponseDTO(child));
		}

		return dto;
	}

	public List<Comment> findAllByPostId(Long postId) {
		return commentRepository.findAllByPostId(postId);
	}

	public void update(Long id, CommentResponseDTO commentDTO) {
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("comment not found"));
		comment.setContent(commentDTO.getContent());
	}

	public void delete(Long id) {
		Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("comment not found"));

		commentRepository.delete(comment);
	}
}
