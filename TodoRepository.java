package com.gm.springboot.web.TestApp.Service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gm.springboot.web.TestApp.model.Todo;


public interface TodoRepository extends JpaRepository<Todo,Integer>{
	
	List<Todo> findByUser(String user);
}
