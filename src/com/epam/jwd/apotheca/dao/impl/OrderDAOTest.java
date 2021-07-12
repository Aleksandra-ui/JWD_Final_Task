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

public class OrderDAOTest {

	private static OrderDAO orderDAO;
	private static DrugDAO drugDAO;
	private static UserDAO userDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		orderDAO = new OrderDAOImpl();
		drugDAO = new DrugDAOImpl();
		userDAO = new UserDAOImpl();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}

	@Test
	public void testSave() {
		
		Order order = new Order();
		Map<Drug, Integer> drugs = new HashMap<>();
		
		for ( int i = 0; i < 3; i ++ ) {
			
			Drug d1 = drugDAO.findDrug("drug"+i, Double.valueOf(i));
			if ( d1 == null ) {
				d1 = new Drug();
				d1.setName("drug"+i);
				d1.setDose(Double.valueOf(i));
				d1.setPrice(i);
				d1.setQuantity(1);
				d1.setPrescription(false);
				d1 = drugDAO.save(d1);
			}
			
			drugs.put(d1, i + 2);
		}
		
		User u = ((UserDAOImpl)userDAO).getUser("g");
		if ( u == null ) {
			u = new User();
			u.setName("g");
			u.setPassword("m");
			Role role = new Role();
			role.setName("client");
			role.setId(3);
			u.setRole(role);
			u = userDAO.save(u);
		}

		order.setDrugs(drugs);
		order.setDate(new Date(System.currentTimeMillis()));
		order.setUserId(u.getId());
		orderDAO.save(order);
		
	}

	@Test
	public void testFindAll() {
		
	}

	@Test
	public void testFindAllById() {
		
	}

	@Test
	public void testUpdate() {
		
	}

	@Test
	public void testDelete() {
		
	}

	@Test
	public void testGetMaxId() {
		
	}

	@Test
	public void testFindOrder() {
		Order order = new Order();
		order.setDate(new Date(System.currentTimeMillis()));
		
		User u = ((UserDAOImpl)userDAO).getUser("kl");
		if ( u == null ) {
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
		for ( int i = 0; i < 3; i ++ ) {
			
			Drug d1 = drugDAO.findDrug("dru"+i, Double.valueOf(i));
			if ( d1 == null ) {
				d1 = new Drug();
				d1.setName("dru"+i);
				d1.setDose(Double.valueOf(i));
				d1.setPrice(i);
				d1.setQuantity(1);
				d1.setPrescription(false);
				d1 = drugDAO.save(d1);
			}
			
			drugs.put(d1, i + 2);
		}

		order.setDrugs(drugs);
		order.setUserId(u.getId());
		order = orderDAO.save(order);
		System.out.println(order.getId());
		Order order2 = orderDAO.findOrder(order.getId());
		Assert.assertEquals(order, order2);
		
	}

}
