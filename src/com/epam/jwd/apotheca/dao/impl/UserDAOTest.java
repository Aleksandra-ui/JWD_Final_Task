package com.epam.jwd.apotheca.dao.impl;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.ConnectionPool;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class UserDAOTest {
	
	static UserDAO userDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userDAO = new UserDAOImpl();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}
	//@Test
	public void testGetUsers() {
		List<User> users = ((UserDAOImpl)userDAO).getUsers();
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

	//@Test
	public void testCreateUser() {
		
		assert  ((UserDAOImpl)userDAO).createUser("ccc", "ccc", "ccc") != null;
		List<User> users =  ((UserDAOImpl)userDAO).getUsers();
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

	//@Test
	public void testCreateModelUser() {
		
		User user = new User();
		user.setName("ccc");
		user.setPassword("ccc");
		user.setRole("ccc");
		assert  ((UserDAOImpl)userDAO).createUser(user) != null;
		List<User> users =  ((UserDAOImpl)userDAO).getUsers();
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

	//@Test
	public void testDeleteUser() {
		
		 ((UserDAOImpl)userDAO).createUser("vt", "h", "h");
		assert  ((UserDAOImpl)userDAO).deleteUser("vt");
		List<User> users =  ((UserDAOImpl)userDAO).getUsers();

		boolean found = false;
		for (User u : users) {
			if ("vt".equalsIgnoreCase(u.getName())) {
				found = true;
				break;
			}
		}
		assert !found;

	}

	//@Test
	public void testHasUser() {
		
		 ((UserDAOImpl)userDAO).createUser("v", "h", "h");
		assert  ((UserDAOImpl)userDAO).hasUser("v");
		
		 ((UserDAOImpl)userDAO).deleteUser("v");
		assert !  ((UserDAOImpl)userDAO).hasUser("v");
	}
	
	@Test
	public void testFindAll() {
		
		boolean result = false;
		
		List<User> users = userDAO.findAll();
		if (users != null) {
			result = true;
		}
		assert result;
		
	}
	@Test
	public void testFindById() {
		
		boolean result = false;
		
		result = userDAO.findById(1) != null;
		
		assert result;
		
	}
	@Test
	public void testSave() {
		
		boolean result = false;
		User user = new User();
		user.setName("in");
		user.setRole("doctor");
		user.setPassword("ni");
		
		result = userDAO.save(user) != null ? true : false;

		assert result;
		
	}
	@Test
	public void testUpdate() {
		
		User user = new User();
		user.setName("kkkkkk");
		user.setPassword("t");
		user.setRole("CLIENT");
		
		user = userDAO.save(user);
		
		user.setPassword("q");
		user.setRole("DOCTOR");
		user.setName("p");
		
		User newUser = userDAO.update(user);
		
		assert "q".equals(newUser.getPassword()) & "DOCTOR".equals(newUser.getRole()) & "p".equals(newUser.getName());
		
	}
	@Test
	public void testDelete() {
		
		boolean result = false;
		User user = new User();
		user.setName("kkkkkk");
		user.setPassword("jjjj");
		user.setRole("CLIENT");
		
		User newUser = userDAO.save(user);
		
		result = userDAO.delete(newUser.getId());
		
		assert result;
		
	}

}
