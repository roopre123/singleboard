package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Board;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.BoardService;
import com.example.service.CommentService;


@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentService commentService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String viewForm(Model model) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		//System.out.println(user.getUsername());
		User userResult = userRepository.findByUsername(user.getUsername());
		System.out.println(userResult.getRole());
		//System.out.println(userResult.toString());
		model.addAttribute("userSession",userResult.getId());
		
		return "board/boardForm";
	}
	
	@RequestMapping(value ="/list" , method=RequestMethod.GET)
	public String list(Model model,@PageableDefault (
			size =10, sort="id", direction= Sort.Direction.DESC ) Pageable pageable) 
	{
		//System.out.println(pageable.getPageNumber());/* 페이지 번호..? */
		//System.out.println(pageable.getPageSize());/* 위에 사이즈 5 */
		//System.out.println(pageable.getSort());
		model.addAttribute("boardList",boardService.findLoadList(pageable)) ;
		
		return "board/boardlist";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(value = "/signup/{userid}", method=RequestMethod.POST)
	public ResponseEntity<Board> singUp(@PathVariable("userid") int userid,@RequestBody Board board) {
		Optional<User> user = userRepository.findById(userid);
		board.setUser(user.get());
		//System.out.println(board.toString());
		boardService.insert(board);
		return new ResponseEntity<Board>(board, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public String viewBoard(@RequestParam(value = "id", defaultValue ="0")Long id, Model model) {
		Board board = boardService.findBoardById(id);
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userResult = userRepository.findByUsername(user.getUsername());
		model.addAttribute("user", userResult);
		model.addAttribute("board",board);
		return "board/boardView";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(value = "/updateForm", method=RequestMethod.POST)
	public String updateForm(Long id,Model model) {
		Board board = boardService.findBoardById(id);
		board.setContent(board.getContent().replace("<br>", "\r\n"));
		model.addAttribute("board",board);
		return "board/boardForm";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@RequestMapping(value = "/update" , method=RequestMethod.PUT )
	public String updateBoard(@RequestBody Board board, Model model) {
		Board boardResult = boardService.findBoardById(board.getId());
		board.setWdate(boardResult.getWdate());
		board.setUser(boardResult.getUser());
		boardService.insert(board);
		model.addAttribute("board", board);
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userResult = userRepository.findByUsername(user.getUsername());
		model.addAttribute("user", userResult);
		return "board/boardView";
	}
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	@Transactional
	@RequestMapping(value = "/delete", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBoard(@RequestBody Board board, Model model) {
		commentService.deleteAllByBoard_id(board.getId());
		boardService.deleteById(board);		
		return new ResponseEntity<>( HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/search", method=RequestMethod.GET)
	public String searchBoard(@RequestParam(value = "search_board") String key,
			@RequestParam(value = "search_value") String value , Model model ,
			@PageableDefault (size =10, sort="id", direction= Sort.Direction.DESC ) Pageable pageable) {
		//System.out.println(key + " : " + value) ;
		Map<String, String> map = new HashMap<>();
		map.put("key", key); map.put("value", value);
		model.addAttribute("map",map);
		if(key.equals("search_username")) {
			Page<Board> boardResult = boardService.findAllByUser(value,pageable);
			model.addAttribute("boardList",boardResult);
		}
		if(key.equals("search_title")) {
			Page<Board> boardResult = boardService.findAllByTitleContaining(value,pageable);
			model.addAttribute("boardList",boardResult);
		}
		
		//paging search_title=제목   search_username=작성자   
		//boardService.findAllByUser()
		//model.addAttribute("boardList",boardService.findLoadList(pageable)) ;
		return "board/searchList";
	}

}
