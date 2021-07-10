package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.User;

public interface UserDAO extends DAO<User> {
	
	static final Integer PHARMACIST = 2;
	static final Integer CLIENT = 1;
	static final Integer DOCTOR = 4;
	
	User findById(Integer id);
	
	List<User> findUsersByRole(Integer roleId);

}
