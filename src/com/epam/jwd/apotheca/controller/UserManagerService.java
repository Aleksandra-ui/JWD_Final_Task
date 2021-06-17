package com.epam.jwd.apotheca.controller;


import javax.servlet.http.HttpSession;

import com.epam.jwd.apotheca.dao.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class UserManagerService {

	private UserDAO userDAO = new UserDAO();
	
	public boolean createUser(User user, HttpSession session) {
		
		boolean result = false;
		if (userDAO.createUser(user)){
			session.setAttribute("user", user);
			result = true;
		} 
		return result;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	
	
}
