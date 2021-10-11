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
	private List<Order> orders;
	private int pageSize;
	private int currentPage;
	
	private Orders() {
	}
	
	public static Orders getInstance() {
		return instance;
	}

	@Override
	public String run() {
		
		orders = OrderManagerService.getInstance().findOrdersByUser(user.getId());
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		
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
	
	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

}
