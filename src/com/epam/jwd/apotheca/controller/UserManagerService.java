package com.epam.jwd.apotheca.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.User;

public class UserManagerService {

	private UserDAO userDAO = new UserDAOImpl();
	
	public boolean createUser(User user, HttpSession session) {
		
		boolean result = false;
		User newUser = userDAO.save(user);
		if ( newUser != null){
			session.setAttribute("user", newUser);
			user = newUser;
			result = true;
		} 
		return result;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public boolean hasUser(String name) {
		boolean contains = false;
		List<User> users = userDAO.findAll();
		for ( User user : users ) {
			if ( name.equals(user.getName()) ) {
				contains=true;
				break;
			}
		}
		return contains;
	}
	
	public List<User> getUsers() {
		return userDAO.findAll();
		
	}
	
}
