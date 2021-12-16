package com.epam.jwd.apotheca.controller;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.dao.impl.OrderDAOImpl;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class OrderManagerService {

	private static OrderManagerService instance = new OrderManagerService();
	private OrderDAO orderDAO = OrderDAOImpl.getInstance();
	private DrugDAO drugDAO = DrugDAOImpl.getInstance();
	
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

	public synchronized Order buy(Integer userId, Map<Drug, Integer> drugs) {
		
		boolean result = true;
		Map<Drug, Drug> updatedDrugs = new HashMap<Drug, Drug>();
		Order order = null;
		if ( drugs != null && ! drugs.isEmpty() ) {
			for ( Drug d : drugs.keySet() ) {
				Drug value = drugDAO.findById(d.getId());
				updatedDrugs.put(d, value);
				result = ((DrugDAOImpl)drugDAO).changeQuantity(value, drugs.get(d));
				if ( ! result  ) {
					revertChanges(updatedDrugs);
					break;
				}
			}
			
			if ( result ) {
				
				order = new Order();
				order.setDate(new Date(GregorianCalendar.getInstance().getTimeInMillis()));
				order.setUserId(userId);
				order.setDrugs(drugs);
				order = orderDAO.save(order);
				if ( order == null ) {
					revertChanges(updatedDrugs);
				}
			}
	
		}
		
		return order;

	}

	private void revertChanges(Map<Drug, Drug> updatedDrugs) {
		for ( Drug ud : updatedDrugs.keySet() ) {
			drugDAO.update(updatedDrugs.get(ud));
		}
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
