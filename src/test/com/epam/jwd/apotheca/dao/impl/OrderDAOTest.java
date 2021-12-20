package com.epam.jwd.apotheca.dao.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;


public class OrderDAOTest {

	private static OrderDAO orderDAO;
	private static DrugDAO drugDAO;
	private static UserDAO userDAO;
	private static User user;
	private static Order order;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		orderDAO = OrderDAOImpl.getInstance();
		drugDAO = DrugDAOImpl.getInstance();
		userDAO = UserDAOImpl.getInstance();
		user = createStandardUser();
		order = createOrder(user);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		orderDAO.delete(order.getId());
		userDAO.delete(user.getId());
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testFindAll() {
		for (Order o : orderDAO.findAll()) {
			System.out.println(o);
		}
	}

	@Test
	public void testFindOrder() {

		Order orderInDB = orderDAO.findOrder(order.getId());
		Assert.assertEquals(order, orderInDB);
		
	}

	@Test
	public void testFindOrdersByUser() {

		Assert.assertEquals(order, ((OrderDAOImpl) orderDAO).findOrdersByUser(user.getId()).get(0));

	}
	
	@Test
	public void testGetTotalCount() {
		
		User user = new User();
		Role role = ((UserDAOImpl)userDAO).findRole(UserDAO.ROLE_NAME_CLIENT);

		user.setName("test user");
		user.setPassword("789");
		user.setRole(role);
		user = userDAO.save(user);
		
		int first = orderDAO.getTotalCount();
		
		Order order2 = createOrder(user);
		
		int second = orderDAO.getTotalCount();

		orderDAO.delete(order2.getId());
		userDAO.delete(user.getId());
		
		assert first == second - 1;
		
	}
	
	@Test
	public void testGetDrugsCountByUser() {
		
		int count = ((OrderDAOImpl) orderDAO).getDrugsCountByUser(user.getId());
		Assert.assertEquals(2, count);
	
	}
	
	private static Order createOrder(User user) {
		
		Order order = new Order();

		order.setDate(new Date(System.currentTimeMillis()));
		order.setUserId(user.getId());
		Map<Drug, Integer> drugs = new HashMap<>();
		drugs.put(drugDAO.findById(1), 2);
		drugs.put(drugDAO.findById(2), 4);
		order.setDrugs(drugs);

		return orderDAO.save(order);
		
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
