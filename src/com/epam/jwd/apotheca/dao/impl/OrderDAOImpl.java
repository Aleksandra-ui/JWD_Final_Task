package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.pool.ConnectionPool;
import com.epam.jwd.apotheca.pool.CouldNotInitializeConnectionPoolException;

public class OrderDAOImpl implements OrderDAO {
	
	private ConnectionPool cp = ConnectionPool.retrieve();
	
	public OrderDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Order save(Order entity) {
		return null;
	}

	@Override
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findAllById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order update(Order entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Integer getMaxId() {
		String query = "select max(id) from mydb.orders";
		Integer id = null;
		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			
			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt(1);
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

}
