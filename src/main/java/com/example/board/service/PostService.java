package com.example.board.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.example.board.domain.ActionType;
import com.example.board.domain.User;
import com.example.board.domain.UserPost;
import com.example.board.dto.LikeResponseDTO;
import com.example.board.dto.PostUpdateDTO;
import com.example.board.error.ResourceNotFoundException;
import com.example.board.repository.UserPostRepository;
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
	private final UserPostRepository userPostRepository;

	public Post save(Post post) {
		return postRepository.save(post);
	}

	//게시글 조회수 올리는 메서드
	public Post viewPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));
		post.increaseHits();
		return post;
	}

	//좋아요 누르지 않았으면 좋아요 개수 1 증가, userPost 생성
	//좋아요 누른 상태이면 좋아요 개수 1 감소, userPost 삭제
	public LikeResponseDTO pushLike(User user, Post post) {
		UserPost userPost = userPostRepository.findByUserIdAAndPostId(user.getId(), post.getId());
		LikeResponseDTO dto = new LikeResponseDTO();
		if (userPost == null) {
			userPostRepository.save(new UserPost(user, post, ActionType.LIKE));
			post.increaseLikes();
			dto.setHasLiked(true);
		} else{
			userPostRepository.delete(userPost);
			post.decreaseLikes();
			dto.setHasLiked(false);
		}
		dto.setLikeCount(post.getLikes());
		return dto;
	}

	public Post findPostById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(()->new ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));
	}
	public List<Post> findAllPosts() {
		return postRepository.findAll();
	}

	public void updatePost(Long id, PostUpdateDTO dto) {
		Post post = postRepository.findById(id).orElseThrow();
		post.setContent(dto.getContent());
		post.setTitle(dto.getTitle());
	}

	public void deletePost(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));

		postRepository.delete(post);
	}

	public PostDTO contertToPostDTO(Post post) {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(post.getId());
		postDTO.setCreateDate(post.getCreateDate());
		postDTO.setNickname(post.getUser().getNickname());
		postDTO.setUsername(post.getUser().getUsername());
		postDTO.setTitle(post.getTitle());
		postDTO.setContent(post.getContent());
		postDTO.setLikes(post.getLikes());
		postDTO.setHits(post.getHits());
		return postDTO;
	}

}
