package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Comment;
import com.example.repository.CommentRepository;

@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public Comment insert(Comment comment) {
		Comment com = commentRepository.save(comment);
		return com;
	}
	
	public List<Comment> searchAllList (){
		List<Comment> commentListAll = commentRepository.findAll();
		return commentListAll; 
	}
	
	public List<Comment> searchAllByBoard(Long boardid){
		List<Comment> commentListOfBoard = commentRepository.findAllByBoard_id(boardid);
		return commentListOfBoard;
	}
	
	public void delete(Long comment_id) {
		commentRepository.deleteById(comment_id);
	}
	
	
	public void deleteAllByBoard_id(Long id){
		commentRepository.deleteAllByBoard_id(id);
	}
	
	public void deleteAllByUser_id(int id) {
		commentRepository.deleteAllByUser_id(id);
	}
 
}
