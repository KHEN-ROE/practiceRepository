package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.User;
import edu.pnu.persistence.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository ur;
	
	public User getUser() {
		return (User) ur.findAll();
	}
	
	
	
	public User addUser(User user) {
		User u = new User();
		u.setEmail(user.getEmail());
		u.setName(user.getName());
		u.setLocale(user.getLocale());
		System.out.println("저장할 user정보 : " + u);
		return ur.save(u);
	}
}
