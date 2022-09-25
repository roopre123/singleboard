package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	public List<Comment> findAllByBoard_id(Long id);
	
	public void deleteAllByBoard_id(Long id);
	
	public void deleteAllByUser_id(int id);
}
