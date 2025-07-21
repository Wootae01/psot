package com.example.board.repository;

import com.example.board.domain.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {

    @Query("SELECT up FROM UserPost up WHERE up.user.id =:userId AND up.post.id =:postId")
    UserPost findByUserIdAAndPostId(Long userId, Long postId);
}
