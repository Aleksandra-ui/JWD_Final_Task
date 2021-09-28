package com.epam.jwd.apotheca.dao.impl;

import org.junit.Assert;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderDAOTest {

	private static OrderDAO orderDAO;
	private static DrugDAO drugDAO;
	private static UserDAO userDAO;
	private static User user;
	private static final Logger logger = LoggerFactory.getLogger(OrderDAOTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		orderDAO = new OrderDAOImpl();
		drugDAO = DrugDAOImpl.getInstance();
		userDAO = new UserDAOImpl();
		user = createStandardUser();
		if ( userDAO.getUser(user.getName()) == null ) {
			user = userDAO.save(user);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testFindAll() {
		for (Order o : orderDAO.findAll()) {
			System.out.println(o);
		}
	}

	@Test
	public void testFindOrder() throws InterruptedException {
		Order order = new Order();
		order.setDate(new Date(System.currentTimeMillis()));

		User u = ((UserDAOImpl) userDAO).getUser("kl");
		if (u == null) {
			u = new User();
			u.setName("kl");
			u.setPassword("m");
			Role role = new Role();
			role.setName("client");
			role.setId(3);
			u.setRole(role);
			u = userDAO.save(u);
		}

		Map<Drug, Integer> drugs = new HashMap<>();
		for (int i = 0; i < 3; i++) {

			Drug d1 = drugDAO.findDrug("dru" + i, Double.valueOf(i));
			if (d1 == null) {
				d1 = new Drug();
				d1.setName("dru" + i);
				d1.setDose(Double.valueOf(i));
				d1.setPrice(i);
				d1.setQuantity(1);
				d1.setPrescription(false);
				d1 = drugDAO.save(d1);
				logger.info("" + d1);
			}

			drugs.put(d1, i + 2);
		}

		order.setDrugs(drugs);
		order.setUserId(u.getId());
		order = orderDAO.save(order);
		//Thread.currentThread().sleep(1000);
		Order order2 = orderDAO.findOrder(order.getId());
		Assert.assertEquals(order, order2);
		
		orderDAO.delete(order.getId());
		for (Drug d : drugs.keySet()) {
			drugDAO.delete(d.getId());
		}
		userDAO.delete(u.getId());
		
	}

	@Test
	public void testFindOrdersByUser() {

		Order order = createOrder(user);
		order = orderDAO.save(order);

		Assert.assertEquals(order, ((OrderDAOImpl) orderDAO).findOrdersByUser(user.getId()).get(0));

		orderDAO.delete(order.getId());
		
	}
	
	@Test
	public void testGetTotalCount() throws InterruptedException {
		
		int first = orderDAO.getTotalCount();
		
		Order order1 = orderDAO.save(createOrder(user));
		Order order2 = orderDAO.save(createOrder(user));
		System.out.println(order1);
		System.out.println(order2);
		
		int second = orderDAO.getTotalCount();
		
		orderDAO.delete(order1.getId());
		orderDAO.delete(order2.getId());
		
		assert first == second - 2;
		
	}
	
	private Order createOrder(User user) {
		
		Order order = new Order();
		User userFromDB = userDAO.getUser(user.getName());
		if ( userFromDB == null ) {
			user = userDAO.save(user);
		} else {
			user.setId(userFromDB.getId());
		}

		order.setDate(new Date(System.currentTimeMillis()));
		order.setUserId(user.getId());
		Map<Drug, Integer> drugs = new HashMap<>();
		drugs.put(drugDAO.findById(1), 2);
		drugs.put(drugDAO.findById(2), 4);
		order.setDrugs(drugs);

		return order;
		
	}
	
	private static User createStandardUser() {
		
		User user = new User();
		Role role = new Role();
		role.setId(UserDAO.ROLE_CLIENT);

		user.setName("Maksim Fiodorov");
		user.setPassword("789");
		user.setRole(role);
		
		return user;
		
	}

}
