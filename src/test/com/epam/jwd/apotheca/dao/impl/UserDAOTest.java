package com.epam.jwd.apotheca.dao.impl;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

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
		
		assert  ((UserDAOImpl)userDAO).createUser("ccc", 3, "ccc") != null;
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
		Role role = new Role();
		role.setId(2);
		role.setName("pharmacist");
		user.setRole(role);
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
		
		 ((UserDAOImpl)userDAO).createUser("vt", 1, "h");
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
		
		 ((UserDAOImpl)userDAO).createUser("v", 2, "h");
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
		
		User user = new User();
		user.setName("in");
		Role role = new Role();
		role.setId(2);
		role.setName("pharmacist");
		user.setRole(role);
		user.setPassword("ni");
		
		user = userDAO.save(user); 

		assert user != null ? true : false;
		
		userDAO.delete(user.getId());
		
	}
	@Test
	public void testUpdate() {
		
		User user = new User();
		user.setName("k2");
		user.setPassword("t");
		Role role = new Role();
		role.setId( UserDAO.PERM_DOCTOR);
		role.setName("doctor");
		user.setRole(role);
		
		user = userDAO.save(user);
		
		role.setId(UserDAO.PERM_CLIENT);
		role.setName("client");
		user.setPassword("q");
		user.setRole(role);
		user.setName("z");
		
		User newUser = userDAO.update(user);
		
		assert "q".equals(newUser.getPassword()) & UserDAO.PERM_CLIENT == newUser.getRole().getId() & "z".equals(newUser.getName());
		
		userDAO.delete(newUser.getId());
		userDAO.delete(user.getId());
		
	}
	
	@Test
	public void testDelete() {
		
		boolean result = false;
		User user = new User();
		user.setName("k");
		user.setPassword("jjjj");
		Role role = new Role();
		role.setId(UserDAO.PERM_CLIENT);
		role.setName("client");
		user.setRole(role);
		
		User newUser = userDAO.save(user);
		
		result = userDAO.delete(newUser.getId());
		
		assert result;
		
	}
	
	@Test
	public void testFindUsersByRole() {
		System.out.println(userDAO.findUsersByRole(UserDAO.PERM_CLIENT));;
	}

}
