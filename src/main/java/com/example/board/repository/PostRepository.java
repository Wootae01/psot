package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.board.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
