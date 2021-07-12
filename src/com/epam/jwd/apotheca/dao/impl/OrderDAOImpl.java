package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.model.Drug;
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
	public Order save(Order order) {
		
		boolean result = false;
		Integer id = getMaxId() + 1;
		Map<Drug, Integer> drugs = order.getDrugs();
		
		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			for ( Drug drug : drugs.keySet() ) {
				result = st.executeUpdate("insert into mydb.orders (id, drug_id, amount, user_id, order_date)"
						+ "values ('" + id + "','" + drug.getId() + "','" + drugs.get(drug) + "','" + order.getUserId() + "','"
						+ order.getDate() + "')") > 0;
			}
			
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Order orderInDB = null;
		if (result) {
			orderInDB = findOrder(id);
		}
		
		return orderInDB;

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

	@Override
	public Order findOrder(Integer id) {
		
		String query = "select o.id, o.order_date, o.user_id, o.drug_id, o.amount, d.name, d.price, d.dose from mydb.orders o "
					 + "join mydb.drugs d on o.drug_id = d.id "
					 + "where o.id = ? "
					 + "order by o.id;";
		Order order = null;
		
		
		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			Map<Drug, Integer> drugs = null;
			while (rs.next()) {
				if ( order == null ) {
					drugs = new HashMap <Drug, Integer> ();
					order = new Order();
					order.setId(rs.getInt("o.id"));
					order.setUserId(rs.getInt("o.user_id"));
					order.setDate(rs.getDate("o.order_date"));
				}
				Drug drug = new Drug();
				drug.setId(rs.getInt("o.drug_id"));
				drug.setName(rs.getString("d.name"));
				drug.setPrice(rs.getInt("d.price"));
				drug.setDose(rs.getDouble("d.dose"));
				drugs.put(drug, rs.getInt("o.amount"));
			}
			if (order != null) {
				order.setDrugs(drugs);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return order;
		
	}

}
