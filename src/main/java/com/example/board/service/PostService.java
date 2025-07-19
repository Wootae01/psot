package com.example.board.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.domain.Post;
import com.example.board.dto.PostDTO;
import com.example.board.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;

	public Post save(Post post) {
		return postRepository.save(post);
	}

	public Post findPostById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(()->new NoSuchElementException("The id of this post does not exist"));
	}
	public List<Post> findAllPosts() {
		return postRepository.findAll();
	}

	public void updatePost(Long id, PostDTO dto) {
		Post post = postRepository.findById(id).orElseThrow();
		post.setContent(dto.getContent());
		post.setTitle(dto.getTitle());
		post.setHits(dto.getHits());
		post.setLikes(dto.getLikes());
	}

	public void deletePost(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("The id of this post does not exist"));

		postRepository.delete(post);
	}

}
