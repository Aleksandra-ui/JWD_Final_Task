package com.epam.jwd.apotheca.dao.impl;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class UserDAOTest {

	private static UserDAO userDAO;
	private static User user;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		userDAO = UserDAOImpl.getInstance();
		user = createStandardUser();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		userDAO.delete(user.getId());
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

		userDAO.delete(user.getId());
		
		user = createStandardUser();

		assert user != null;

	}

	@Test
	public void testUpdate() {

		user.setPassword("q");
		user.setName("z");

		User newUser = userDAO.update(user);
		
		assert "q".equals(newUser.getPassword()) && "z".equals(newUser.getName());
		
		user.setName("Maksim Fiodorov");
		user.setPassword("789");
		user = userDAO.update(user);

	}

	@Test
	public void testDelete() {

		assert userDAO.delete(user.getId());
		
		user = createStandardUser();

	}

	@Test
	public void testFindUsersByRole() {
		System.out.println(userDAO.findUsersByRole(UserDAO.ROLE_NAME_CLIENT));
	}

	@Test
	public void testChangeRole() {
		
		Role role1 = user.getRole();
		
		user = ((UserDAOImpl)userDAO).changeRole(user.getId(), UserDAO.ROLE_NAME_DOCTOR);
		
		Role role2 = user.getRole();
		
		user = ((UserDAOImpl)userDAO).changeRole(user.getId(), UserDAO.ROLE_NAME_CLIENT);
		
		Assert.assertNotEquals(role1, role2);
		
	}
	
	@Test
	public void testFindRole() {
		
		Role role = ((UserDAOImpl)userDAO).findRole(UserDAO.ROLE_NAME_DOCTOR);
		Assert.assertEquals(UserDAO.ROLE_NAME_DOCTOR, role.getName());
		Assert.assertEquals(UserDAO.ROLE_DOCTOR, role.getId());
		Integer expectedPerm = UserDAO.PERM_CLIENT + UserDAO.PERM_DOCTOR;
		Assert.assertEquals(expectedPerm, role.getPermission());
		
	}
	
	static User createStandardUser() {
		
		User user = new User();
		Role role = ((UserDAOImpl)userDAO).findRole(UserDAO.ROLE_NAME_CLIENT);

		user.setName("Maksim Fiodorov");
		user.setPassword("789");
		user.setRole(role);
		
		user = userDAO.save(user);
		return user;
		
	}

}