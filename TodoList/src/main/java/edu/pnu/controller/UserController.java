package edu.pnu.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.User;
import edu.pnu.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService us;
	
	@GetMapping("/getUser")
	public List<User> getUser(){
		return (List<User>) us.getUser();
	}
	
	@PostMapping("/addUser")
	public User addUser(@RequestBody User user){
		System.out.println("받은 user 정보 : "+ user);
		return us.addUser(user);
	}
	
}
