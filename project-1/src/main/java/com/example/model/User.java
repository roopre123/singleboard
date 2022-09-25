package com.example.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String eamil;
	private String role;
	@CreationTimestamp
	private Timestamp createDate;
	
	public User() { }	
	
	
	
	
	public User(String username, String password, String eamil, String role) {
		super();
		this.username = username;
		this.password = password;
		this.eamil = eamil;
		this.role = role;
	}
	
}
