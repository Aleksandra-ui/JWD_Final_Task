package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.User;

public interface UserDAO extends DAO<User> {
	
	public static final Integer PERM_PHARMACIST = 2;
	public static final Integer PERM_CLIENT = 1;
	public static final Integer PERM_DOCTOR = 4;
	public static final Integer PERM_ADMIN = 8;
	public static final Integer ROLE_PHARMACIST = 2;
	public static final Integer ROLE_CLIENT = 3;
	public static final Integer ROLE_DOCTOR = 1;
	public static final Integer ROLE_ADMIN = 4;
	public static final String NAME_SUPER_DOC = "super_doc";
	
	User findById(Integer id);
	
	List<User> findUsersByRole(Integer roleId);
	
	User getUser(String name);

}
