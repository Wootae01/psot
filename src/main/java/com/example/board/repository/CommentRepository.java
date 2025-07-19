package com.example.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.board.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment  c WHERE c.post.id =:postId")
	List<Comment> findAllByPostId(Long postId);

	@Query("SELECT c FROM Comment c WHERE c.post.id =:postId AND c.parent IS NULL ORDER BY c.createDate")
	List<Comment> findRootComments(Long postId);
}
