package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Board;
import com.example.model.Comment;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.BoardService;
import com.example.service.CommentService;

@Controller
@RequestMapping("/re")
public class CommentController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/insert")
	public ResponseEntity<Void> insertComment(@RequestParam(value = "content") String content,
								@RequestParam(value = "board_id") Long board_id, Model model, Comment comment) {
		
		Board board = boardService.findBoardById(board_id);
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userResult = userRepository.findByUsername(user.getUsername());
		
		comment.setBoard(board); 
		comment.setUser(userResult);
		comment.setComment(content);

		Comment com = commentService.insert(comment);
		//System.out.println("test");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchAll")
	public String searchList(Model model,@RequestParam(value = "board_id") Long board_id) {
		List<Comment> commentList= commentService.searchAllByBoard(board_id);
		model.addAttribute("commentList", commentList);
		
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userResult = userRepository.findByUsername(user.getUsername());
		model.addAttribute("user", userResult);
		return "comment/commentView";
	}
	
	@RequestMapping(value = "/delete" ,method = RequestMethod.POST)
	public ResponseEntity<Void> deleteComment(@RequestParam(value = "comment_id") Long comment_id) {
		commentService.delete(comment_id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
