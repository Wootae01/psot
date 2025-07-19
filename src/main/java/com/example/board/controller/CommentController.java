package com.example.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.board.domain.Comment;
import com.example.board.domain.User;
import com.example.board.service.CommentService;
import com.example.board.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final UserService userService;

}
