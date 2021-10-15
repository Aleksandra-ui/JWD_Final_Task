package com.epam.jwd.apotheca.controller;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.dao.impl.OrderDAOImpl;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class OrderManagerService {

	private static OrderManagerService instance = new OrderManagerService();
	private OrderDAO orderDAO = OrderDAOImpl.getInstance();
	
	private OrderManagerService() {
		
	}
	
	public static OrderManagerService getInstance() {
		return instance;
	}

	public Integer getTotalAmount(Integer orderId) {

		Order order = orderDAO.findOrder(orderId);
		return order.getDrugs().values().stream().mapToInt(Integer::intValue).sum();

	}

	public Order findOrder(Integer id) {

		return orderDAO.findOrder(id);

	}

	public List<Order> findOrdersByUser(Integer userId) {

		return ((OrderDAOImpl) orderDAO).findOrdersByUser(userId);

	}

	public Order buy(Integer userId, Map<Drug, Integer> drugs) {
		//здесь обратиться к drugDAO и изменить quantity
		Order order = null;
		if (drugs != null && !drugs.isEmpty()) {
			order = new Order();
			order.setDate(new Date(GregorianCalendar.getInstance().getTimeInMillis()));
			order.setUserId(userId);
			order.setDrugs(drugs);
			order = orderDAO.save(order);
		}
		return order;

	}
	
	public Integer getDrugsCountByUser(Integer userId) {
		return ((OrderDAOImpl) orderDAO).getDrugsCountByUser(userId);
	}

	public List<Map<String, String>> findDrugInfoByRange(User user, int start, int count) {
		return ((OrderDAOImpl) orderDAO).findDrugInfoByRange(user, start, count);
	}
	
	public boolean deleteUserOrders(User user) {
		boolean result = true;
		List<Order> orders = ((OrderDAOImpl) orderDAO).findOrdersByUser(user.getId());
		for ( Order order : orders ) {
			result &= ((OrderDAOImpl) orderDAO).delete(order.getId());
		}
		return result;
	}
	
}
