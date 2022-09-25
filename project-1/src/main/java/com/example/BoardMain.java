package com.example;

import java.util.Date;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.model.Board;
import com.example.model.BoardType;
import com.example.model.User;
import com.example.repository.BoardRepository;
import com.example.repository.UserRepository;

//@SpringBootApplication
public class BoardMain implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BoardMain.class,args);
	}
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(1);
		
		IntStream.rangeClosed(1, 200).forEach(index -> boardRepository.save(Board.builder()
				.title("타이틀" + index)
				.subTitle("순서" + index)
				.content("콘텐츠")
				.boardType(BoardType.free)
				.wdate(new Date())
				.user(user.get()).build()));
	}
}
