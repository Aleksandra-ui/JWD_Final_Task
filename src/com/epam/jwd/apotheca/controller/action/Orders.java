package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class Orders implements RunCommand {

	private static Orders instance = new Orders();
	private static final Logger logger = LoggerFactory.getLogger(Orders.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	List<Order> orders;
	
	private Orders() {
		
		orders = new ArrayList<Order>();
		
	}
	
	public static Orders getInstance() {
		return instance;
	}

	@Override
	public String run() {
		
		orders.addAll(OrderManagerService.getInstance().findOrdersByUser(user.getId()));
		
		return actionTime;
		
	}

	@Override
	public String getView() {
		return "secure/orders1.jsp";
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	public String getActionTime() {
		return actionTime;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	public List<Order> getOrders() {
		return orders;
	}

}
