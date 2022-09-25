package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Board;

public interface BoardRepository  extends JpaRepository<Board, Long>{
	public Page<Board> findAllByUser_username(String uname,Pageable pageable);
	
	public Page<Board> findAllByTitleContaining(String key,Pageable pageable);
	
	public void deleteAllByUser_id(int id);
	
	public List<Board> findIdByUser_id(int id);
}
