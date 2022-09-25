package com.example.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "commentid")
	private Long id;
	private String comment;
	private Date cdate;
	
	@PrePersist
	public void beforeCreate() { 
		cdate = new Date();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	
}
