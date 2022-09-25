package com.example.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.model.Board;
import com.example.repository.BoardRepository;


@Service
public class BoardService {
	
	private final BoardRepository boardRepository;
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public Board findBoardById(Long id) {
		return boardRepository.findById(id).orElse(new Board());
	}
	
	public Page<Board> findLoadList(Pageable pageable) {
		pageable = PageRequest.of(
				pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() -1, 
				pageable.getPageSize(), 
				pageable.getSort());
		return  boardRepository.findAll(pageable);
	}
	
	public Board insert(Board board) {
		Board boardResult = boardRepository.save(board);
		System.out.println("BoardService.insert() : " + boardResult.toString());
		return boardResult;
	}
	
	public void deleteById(Board board) {
		boardRepository.deleteById(board.getId());
	}
	
	public Page<Board> findAllByUser(String value,Pageable pageable){
		pageable = PageRequest.of(
				pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() -1, 
				pageable.getPageSize(), 
				pageable.getSort());
		Page<Board> search_board_list = boardRepository.findAllByUser_username(value, pageable);
		
		return search_board_list;
	}
	
	public Page<Board> findAllByTitleContaining(String key,Pageable pageable){
		pageable = PageRequest.of(
				pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() -1, 
				pageable.getPageSize(), 
				pageable.getSort());
		Page<Board> listTitleContain = boardRepository.findAllByTitleContaining(key, pageable);
		return listTitleContain;
	} 
	
	public void deleteAllByUser(int userid) {
		boardRepository.deleteAllByUser_id(userid);
	}
	
	public List<Board> findIdByUser_id(int userid){
		return boardRepository.findIdByUser_id(userid);
	}

}
