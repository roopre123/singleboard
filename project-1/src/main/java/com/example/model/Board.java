package com.example.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Board {  
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "boardid")
	private Long id;
	private String title;
	private String subTitle;
	private String content;
	@Enumerated(EnumType.STRING)
	private BoardType boardType;
	private Date wdate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private User user;
	
	//, orphanRemoval = true   mappedBy = "board" ,cascade = CascadeType.ALL
	
	@PrePersist
	public void beforeCreate() {
		wdate = new Date();  
	}
	
	@Builder
	public Board(String title, String subTitle, String content, BoardType boardType, Date wdate, User user) {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.content = content;
		this.boardType = boardType;
		this.wdate = wdate;
		this.user = user;
	}
}
