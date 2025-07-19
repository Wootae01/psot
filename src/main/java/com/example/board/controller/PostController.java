package com.example.board.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.domain.Comment;
import com.example.board.domain.Post;
import com.example.board.domain.User;
import com.example.board.dto.CommentResponseDTO;
import com.example.board.dto.PostAddDTO;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
import com.example.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final UserService userService;
	private final CommentService commentService;

	@GetMapping("/post")
	public String getAllPost(Model model) {
		List<Post> posts = postService.findAllPosts();
		model.addAttribute("posts",posts);
		return "home";
	}

	@GetMapping("/post/{postId}")
	public String getPost(@PathVariable Long postId, Model model) {
		Post post = postService.findPostById(postId);
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

	@PostMapping("/post/new")
	public String savePost(PostAddDTO dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Post post = new Post();
		User user = userService.findByUsername(username);
		if (user == null) {
			//에러
		}

		post.setUser(user);
		post.setContent(dto.getContent());
		post.setTitle(dto.getTitle());
		post.setHits(0);
		post.setLikes(0);
		postService.save(post);
		return "redirect:/post";
	}

	@PostMapping("/post/{postId}/comments")
	public String saveComment(@PathVariable Long postId, @RequestParam String content,
		@RequestParam(required = false, value = "parentCommentId") Long parentCommentId, Principal principal) {

		Comment parent = null;
		if (parentCommentId != null) {
			parent = commentService.findById(parentCommentId);
		}
		Post post = postService.findPostById(postId);
		String username = principal.getName();
		User user = userService.findByUsername(username);

		commentService.save(user, post, parent, content, 0);
		return "redirect:/post/{postId}";

	}
}
