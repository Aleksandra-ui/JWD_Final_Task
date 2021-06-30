package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.User;

public interface UserDAO extends DAO<User> {
	
	static final String PHARMACIST = "pharmacist";
	static final String CLIENT = "client";
	static final String DOCTOR = "doctor";
	
	User findById(Integer id);
	
	List<User> findUsersByRole(String role);

}
