package com.epam.jwd.apotheca.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.User;

public class UserManagerService {

	private static UserManagerService instance = new UserManagerService();
	private UserDAO userDAO = UserDAOImpl.getInstance();

	private UserManagerService() {
		
	}
	
	public static UserManagerService getInstance() {
		return instance;
	}
	
	public boolean createUser(User user, HttpSession session) {

		boolean result = false;
		User newUser = userDAO.save(user);
		if (newUser != null) {
			session.setAttribute("user", newUser);
			user = newUser;
			result = true;
		}
		return result;
	}
	
	public boolean createUser(User user) {

		boolean result = false;
		if ( UserDAO.NAME_SUPER_DOC.equals( user.getName() ) ) {
			return false;
		}
		User newUser = userDAO.save(user);
		if (newUser != null) {
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
		for (User user : users) {
			if (user.getName().equals(name)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public List<User> getUsers() {
		
		List<User> users = userDAO.findAll();
		users.removeIf(u -> UserDAO.NAME_SUPER_DOC.equals(u.getName()));
		return users;

	}

	public List<User> getClients() {
		return userDAO.findUsersByRole(UserDAO.ROLE_CLIENT);
	}

	public User getUser(String name) {
		
		if ( UserDAO.NAME_SUPER_DOC.equals( name ) ) {
			return null;
		} 
		return ((UserDAOImpl) userDAO).getUser(name);
		
	}

	public User getUser(Integer id) {
		
		User user = userDAO.findById(id);
		if ( user != null && UserDAO.NAME_SUPER_DOC.equals( user.getName() ) ) {
			user = null;
		}
		return user;
		
	}

	public boolean canPrescribe(User user) {

		return isRoleEnabled(user, UserDAO.PERM_DOCTOR);

	}

	public boolean canAddDrugs(User user) {

		return isRoleEnabled(user, UserDAO.PERM_PHARMACIST);

	}

	public boolean isRoleEnabled(User user, Integer permission) {

		return (user != null) && ((permission & user.getRole().getPermission()) == permission);

	}
	
	public boolean deleteUser(Integer userId) {
		
		boolean result = false; 
		User user = userDAO.findById(userId);
		if ( user != null && ! UserDAO.NAME_SUPER_DOC.equals(user.getName()) ) {
			result = userDAO.delete(userId);
		}
		return result;

	}
	
	public List<User> findUsersByRange(int start, int count) {
		
		List<User> users = ((UserDAOImpl)userDAO).findByRange(start, count, true);
		
		return users;

	}

	public int getTotalCount() {
		return ((UserDAOImpl)userDAO).getTotalCount(true);
	}
	
	public User changeRole(Integer userId, Integer roleId) {
		return ((UserDAOImpl)userDAO).changeRole(userId, roleId);
	}

}
