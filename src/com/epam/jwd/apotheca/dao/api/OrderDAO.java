package com.epam.jwd.apotheca.dao.api;

import com.epam.jwd.apotheca.model.Order;

public interface OrderDAO extends DAO<Order> {

	Order findOrder (Integer id);
	
}
