package com.example.board.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.board.domain.*;
import com.example.board.dto.CommentRequestDTO;
import com.example.board.dto.LikeResponseDTO;
import com.example.board.oauth2.CustomOauth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.dto.CommentResponseDTO;
import com.example.board.dto.PostAddDTO;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
import com.example.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final UserService userService;
	private final CommentService commentService;

	//게시글 작성 화면 반환
	@GetMapping("/post")
	public String getAllPost(Model model) {
		List<Post> posts = postService.findAllPosts();
		model.addAttribute("posts",posts);
		return "home";
	}


	//해당 게시글 정보 반환
	@GetMapping("/post/{postId}")
	public String getPost(@PathVariable Long postId, Model model,
						  @AuthenticationPrincipal CustomOauth2User oauth2User) {
		Post post = postService.viewPost(postId);
		User user = null;

		if(oauth2User != null){
			user = userService.findByUsername(oauth2User.getUsername());
		}

		//사용자가 좋아요 눌렀는지 확인
		if (user != null) {
			boolean hasLiked = userService.hasLiked(user, postId);
			model.addAttribute("hasLiked", hasLiked);
		}

		//해당 게시물의 댓글 찾기
		List<Comment> rootComments = commentService.findRootComments(postId);
		List<CommentResponseDTO> response = new ArrayList<>();
		for (Comment rootComment : rootComments) {
			CommentResponseDTO dto = commentService.convertToCommentResponseDTO(rootComment);
			response.add(dto);
		}


		model.addAttribute("post", post);
		model.addAttribute("comments", response);
		return "post";
	}

	/**
	 * 게시글 작성 화면 이동 하는 엔드포인트
	 *
	 * @return 게시글 글쓰기 화면
	 */

	@GetMapping("/post/new")
	public String writePost() {
		return "writePost";
	}

	//게시글 등록
	@PostMapping("/post/new")
	public String savePost(PostAddDTO dto, @AuthenticationPrincipal CustomOauth2User customOauth2User) {

		//로그인 하지 않은 사용자인 경우
		if (customOauth2User == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}

		String username = customOauth2User.getUsername();

		Post post = new Post();
		User user = userService.findByUsername(username);

		post.setUser(user);
		post.setContent(dto.getContent());
		post.setTitle(dto.getTitle());
		post.setHits(0);
		post.setLikes(0);
		postService.save(post);
		return "redirect:/post";
	}

	//댓글 작성
	@ResponseBody
	@PostMapping("/post/{postId}/comments")
	public CommentResponseDTO saveComment(@PathVariable Long postId,
							  @RequestBody CommentRequestDTO requestDTO,
							  @AuthenticationPrincipal CustomOauth2User customOauth2User) {

		Comment parent = null;
		Long parentCommentId = requestDTO.getParentCommentId();
		String content = requestDTO.getContent();

		if (parentCommentId != null) {
			parent = commentService.findById(parentCommentId);
		}
		Post post = postService.findPostById(postId);
		String username = customOauth2User.getUsername();
		User user = userService.findByUsername(username);

		Comment save = commentService.save(user, post, parent, content, 0);
		CommentResponseDTO dto = commentService.convertToCommentResponseDTO(save);

		return dto;

	}

	@PostMapping("/post/{postId}/like")
	public ResponseEntity<LikeResponseDTO> likePost(@PathVariable Long postId, @AuthenticationPrincipal CustomOauth2User oauth2User) {
		if (oauth2User == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}
		String username = oauth2User.getUsername();
		User user = userService.findByUsername(username);
		Post post = postService.findPostById(postId);

		LikeResponseDTO dto = postService.pushLike(user, post);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}
