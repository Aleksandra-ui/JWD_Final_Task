package com.epam.jwd.apotheca.dao.impl;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class UserDAOTest {

	static UserDAO userDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userDAO = UserDAOImpl.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testGetUsers() {
		List<User> users = ((UserDAOImpl) userDAO).getUsers();
		assert users.size() > 0;

		boolean found = false;
		for (User user : users) {
			if ("Tatyana Lebedeva".equalsIgnoreCase(user.getName())) {
				found = true;
				break;
			}
		}

		assert found;
	}

	@Test
	public void testCreateUser() {

		User user = ((UserDAOImpl) userDAO).createUser("ccc", 3, "ccc"); 
		assert user != null;
		List<User> users = ((UserDAOImpl) userDAO).getUsers();
		assert users.size() > 0;

		boolean found = false;
		for (User u : users) {
			if ("ccc".equalsIgnoreCase(u.getName())) {
				found = true;
				break;
			}
		}
		
		userDAO.delete(user.getId());
		
		assert found;

	}

	@Test
	public void testDeleteUser() {

		((UserDAOImpl) userDAO).createUser("vt", 1, "h");
		assert ((UserDAOImpl) userDAO).deleteUser("vt");

	}

	@Test
	public void testHasUser() {

		((UserDAOImpl) userDAO).createUser("v", 2, "h");
		assert ((UserDAOImpl) userDAO).hasUser("v");

		((UserDAOImpl) userDAO).deleteUser("v");
		assert !((UserDAOImpl) userDAO).hasUser("v");
		
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

		User user = createStandardUser();
		
		userDAO.delete(user.getId());

		assert user != null ? true : false;

	}

	@Test
	public void testUpdate() {

		User user = createStandardUser();

		user.setPassword("q");
		user.setName("z");

		User newUser = userDAO.update(user);
		
		userDAO.delete(newUser.getId());
		userDAO.delete(user.getId());

		assert "q".equals(newUser.getPassword()) && "z".equals(newUser.getName());

	}

	@Test
	public void testDelete() {

		User user = createStandardUser();

		boolean result = userDAO.delete(user.getId());

		assert result;

	}

	@Test
	public void testFindUsersByRole() {
		System.out.println(userDAO.findUsersByRole(UserDAO.PERM_CLIENT));
	}
	
	@Test
	public void testGetTotalCount() {
		
		int first = userDAO.getTotalCount();
		
		User user = createStandardUser();
		
		int second = userDAO.getTotalCount();
		
		userDAO.delete(user.getId());
		
		assert first == second - 1;
		
	}
	
	static User createStandardUser() {
		
		User user = new User();
		Role role = new Role();
		role.setId(UserDAO.ROLE_CLIENT);
		role.setName("client");
		role.setPermission(UserDAO.PERM_CLIENT);

		user.setName("Maksim Fiodorov");
		user.setPassword("789");
		user.setRole(role);
		
		user = userDAO.save(user);
		System.out.println("test user in DB: " + user);
		return user;
		
	}

}
