package com.epam.jwd.apotheca.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.OrderDAOImpl;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class OrderManagerService {

	private OrderDAO orderDAO = new OrderDAOImpl();
	
	public Integer getTotalAmount(Integer orderId) {
	
		Order order = orderDAO.findOrder(orderId);
		return order.getDrugs().values().stream().mapToInt(Integer::intValue).sum();
		
	}
	
	public Order findOrder(Integer id) {
		
		return orderDAO.findOrder(id);
		
	}
	
	public List<Order> findOrdersByUser(Integer userId) {
		
		return ((OrderDAOImpl)orderDAO).findOrdersByUser(userId);
		
	}
	
}
