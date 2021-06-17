package com.epam.jwd.apotheca.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.epam.jwd.apotheca.model.User;

public class UserDAOTest {

	@Test
	public void testGetUsers() {
		UserDAO userDAO = new UserDAO();
		List<User> users = userDAO.getUsers();
		assert users.size() > 0;

		boolean found = false;
		for (User user : users) {
			if ("b".equalsIgnoreCase(user.getName())) {
				found = true;
				break;
			}
		}

		assert found;
		System.gc();
	}

	@Test
	public void testCreateUser() {
		UserDAO userDAO = new UserDAO();
		assert userDAO.createUser("ccc", "ccc", "ccc");
		List<User> users = userDAO.getUsers();
		assert users.size() > 0;

		boolean found = false;
		for (User user : users) {
			if ("ccc".equalsIgnoreCase(user.getName())) {
				found = true;
				break;
			}
		}
		assert found;

	}

	@Test
	public void testCreateModelUser() {
		UserDAO userDAO = new UserDAO();
		User user = new User();
		user.setName("ccc");
		user.setPassword("ccc");
		user.setRole("ccc");
		assert userDAO.createUser(user);
		List<User> users = userDAO.getUsers();
		assert users.size() > 0;

		boolean found = false;
		for (User u : users) {
			if ("ccc".equalsIgnoreCase(u.getName())) {
				found = true;
				break;
			}
		}
		assert found;

	}

	@Test
	public void testDeleteUser() {
		UserDAO userDAO = new UserDAO();
		userDAO.createUser("vt", "h", "h");
		assert userDAO.deleteUser("vt");
		List<User> users = userDAO.getUsers();

		boolean found = false;
		for (User u : users) {
			if ("vt".equalsIgnoreCase(u.getName())) {
				found = true;
				break;
			}
		}
		assert !found;

	}

	@Test
	public void testHasUser() {
		UserDAO userDAO = new UserDAO();
		userDAO.createUser("v", "h", "h");
		assert userDAO.hasUser("v");
		
		userDAO.deleteUser("v");
		assert ! userDAO.hasUser("v");
	}

}
