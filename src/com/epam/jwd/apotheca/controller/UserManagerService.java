package com.epam.jwd.apotheca.controller;

import java.util.List;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;

public class UserManagerService {

	private static UserManagerService instance = new UserManagerService();
	private UserDAO userDAO = UserDAOImpl.getInstance();

	private UserManagerService() {
	}
	
	public static UserManagerService getInstance() {
		return instance;
	}
	
//	public boolean createUser(User user, HttpSession session) {
//
//		boolean result = false;
//		User newUser = userDAO.save(user);
//		if ( newUser != null ) {
//			session.setAttribute("user", newUser);
//			user = newUser;
//			result = true;
//		}
//		return result;
//		
//	}
	
	public User createUser(User user) {

		if ( UserDAO.ROLE_NAME_SUPER_DOC.equals( user.getName() ) ) {
			return null;
		}
		return userDAO.save(user);
		
	}

	public boolean hasUser(String name) {
		
		return ((UserDAOImpl)userDAO).hasUser(name);
		
	}

	public List<User> getUsers() {
		
		List<User> users = userDAO.findAll();
		users.removeIf(u -> UserDAO.ROLE_NAME_SUPER_DOC.equals(u.getName()));
		return users;

	}

	public List<User> getClients() {
		return userDAO.findUsersByRole(UserDAO.ROLE_NAME_CLIENT);
	}

	public User getUser(String name) {
		
		if ( UserDAO.ROLE_NAME_SUPER_DOC.equals( name ) ) {
			return null;
		} 
		return ((UserDAOImpl) userDAO).getUser(name);
		
	}

	public User getUser(Integer id) {
		
		User user = userDAO.findById(id);
		if ( user != null && UserDAO.ROLE_NAME_SUPER_DOC.equals( user.getName() ) ) {
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
		if ( user != null && ! UserDAO.ROLE_NAME_SUPER_DOC.equals(user.getName()) ) {
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
	
	public User changeRole(Integer userId, String roleName) {
		return ((UserDAOImpl)userDAO).changeRole(userId, roleName);
	}
	
	public Role findRole( String name ) {
		return ((UserDAOImpl)userDAO).findRole( name );
	}

}
