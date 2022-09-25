package com.example.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Board;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.BoardService;
import com.example.service.CommentService;



@Controller
public class UserController {

	@Autowired
	private BoardService boardService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value = {"","/"}, method = RequestMethod.GET)
	public String hello() {
		return "index";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@RequestMapping(value="/manager", method=RequestMethod.GET)
	public @ResponseBody String manager() {
		return "manager";
	}
	
	@RequestMapping(value="/joinForm", method=RequestMethod.GET)
	public String joinForm() {
		return "joinForm";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(User user) {
		boolean userExists = userRepository.existsByUsername(user.getUsername());
		
		if(userExists) {
			return "joinForm";
		}
		else {
			user.setRole("ROLE_USER");
			//System.out.println(user.toString());
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			//System.out.println(user.getPassword());
			userRepository.save(user);
			return "redirect:/loginForm";
		}
		
	}
	
	@RequestMapping(value="/loginForm", method=RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}
	
	@Secured("ROLE_USER") //하나만 조건 걸 수 있
	@RequestMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	//조건을 두개 이상 걸 수 있음
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	@RequestMapping("/data")
	public @ResponseBody String data() {
		return "data";
	}
	
	
	@RequestMapping("/unregisterForm")
	public String unregisterForm() {
		return "unregisterForm";
	}
	
	@Transactional
	@RequestMapping(value = "/deleteUser" , method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@RequestBody User user) {
		System.out.println(user.toString());
		User dbUser = userRepository.findByUsername(user.getUsername());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(user.getPassword(), dbUser.getPassword())) {
			commentService.deleteAllByUser_id(dbUser.getId());
			List <Board> listBoard = boardService.findIdByUser_id(dbUser.getId());
			for (Board board : listBoard) {
				commentService.deleteAllByBoard_id(board.getId());
			}
			boardService.deleteAllByUser(dbUser.getId());
			userRepository.deleteById(dbUser.getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
