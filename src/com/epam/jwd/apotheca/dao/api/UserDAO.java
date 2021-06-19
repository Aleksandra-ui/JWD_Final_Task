package com.epam.jwd.apotheca.dao.api;

import com.epam.jwd.apotheca.model.User;

public interface UserDAO extends DAO<User> {
	
	User findById(Integer id);

}
