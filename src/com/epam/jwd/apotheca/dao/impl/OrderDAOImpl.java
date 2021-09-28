package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.OrderDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class OrderDAOImpl implements OrderDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

	public OrderDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order save(Order order) {

		boolean result = false;
		Integer id = getMaxId() + 1;
		Map<Drug, Integer> drugs = order.getDrugs();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			for (Drug drug : drugs.keySet()) {
				result = st.executeUpdate("insert into mydb.orders (id, drug_id, amount, user_id, order_date)"
						+ "values ('" + id + "','" + drug.getId() + "','" + drugs.get(drug) + "','" + order.getUserId()
						+ "','" + order.getDate() + "')") > 0;
			}

			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to save an order");
			e.printStackTrace();
		} catch (NullPointerException e) {
			logger.error("catched  exception while attempting to save an order");
			e.printStackTrace();
		}

		Order orderInDB = null;
		if (result) {
			orderInDB = findOrder(id);
		}

		logger.info("saved an order");
		return orderInDB;

	}

	@Override
	public List<Order> findAll() {

		List<Order> orders = new ArrayList<Order>();
		Set<Integer> ids = new HashSet<Integer>();
		DrugDAO drugDAO = DrugDAOImpl.getInstance();
		String query1 = "select id from mydb.orders";
		String query2 = "select drug_id, amount, user_id, order_date from mydb.orders where id = ?";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query1);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all orders");
			e.printStackTrace();
		}

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query2)) {

			for (Integer id : ids) {
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				Order order = new Order();
				Map<Drug, Integer> drugs = new HashMap<>();
				order.setId(id);
				if (rs.next()) {
					order.setUserId(rs.getInt("user_id"));
					order.setDate(rs.getDate("order_date"));
					drugs.put(drugDAO.findById(rs.getInt("drug_id")), rs.getInt("amount"));
				}
				while (rs.next()) {
					drugs.put(drugDAO.findById(rs.getInt("drug_id")), rs.getInt("amount"));
				}
				order.setDrugs(drugs);
				orders.add(order);

			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all orders");
			e.printStackTrace();
		}

		logger.info("find all orders");
		return orders;

	}

	@Override
	public Order update(Order order) {
		
		boolean result = true;
		String query = "update mydb.orders set amount = ?, order_date = ?"
				+ "where id = ? and drug_id = ? and user_id = ?";
		
		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query)) {
			connection.setAutoCommit(false);
			st.setInt(3, order.getId());
			st.setInt(5, order.getUserId());
			st.setDate(2, order.getDate());
			
			for (Map.Entry<Drug, Integer> e : order.getDrugs().entrySet()) {
				st.setInt(4, e.getKey().getId());
				st.setInt(1, e.getValue());
				if ( st.executeUpdate() <= 0 ) {
					result = false;
				}
				connection.commit();
			}			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update an order");
			e.printStackTrace();
		}
		
		logger.info("updated an order");
		return result ? order : null;
		
	}

	@Override
	public boolean delete(Integer id) {
		boolean result = false;
		String query = "delete from mydb.orders where id = ?";

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query)) {
			connection.setAutoCommit(false);
			st.setInt(1, id);

			result = st.executeUpdate() > 0;
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete an order");
			e.printStackTrace();
		}

		logger.info("deleted an order");
		return result;
	}

	private Integer getMaxId() {
		String query = "select max(id) from mydb.orders";
		Integer id = null;
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Order findOrder(Integer id) {

		String query = "select o.id, o.order_date, o.user_id, o.drug_id, o.amount, d.name, d.price, d.dose, d.quantity, d.prescription from mydb.orders o "
				+ "join mydb.drugs d on o.drug_id = d.id " + "where o.id = " + id + " order by o.id";
		Order order = null;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(query);
			Map<Drug, Integer> drugs = null;
			while (rs.next()) {
				if (order == null) {
					drugs = new HashMap<Drug, Integer>();
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
				drug.setPrescription(rs.getBoolean("d.prescription"));
				drug.setQuantity(rs.getInt("d.quantity"));
				drugs.put(drug, rs.getInt("o.amount"));
			}
			if (order != null) {
				order.setDrugs(drugs);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find an order by id");
			e.printStackTrace();
		}

		logger.info("found an order by id");
		return order;

	}

	@Override
	public List<Order> findOrdersByUser(Integer userId) {

		Set<Integer> ids = new HashSet<Integer>();
		List<Order> orders = new ArrayList<Order>();
		DrugDAO drugDAO = DrugDAOImpl.getInstance();
		String query1 = "select id from mydb.orders where user_id = ? order by id";
		String query2 = "select drug_id, amount, order_date from mydb.orders where id = ?";

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection
						.prepareStatement(query1);) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find orders by users");
			e.printStackTrace();
		}

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection
						.prepareStatement(query2);) {

			for (Integer id : ids) {
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				Order order = new Order();
				Map<Drug, Integer> drugs = new HashMap<>();
				order.setId(id);
				order.setUserId(userId);
				if (rs.next()) {
					order.setDate(rs.getDate("order_date"));
					drugs.put(drugDAO.findById(rs.getInt("drug_id")), rs.getInt("amount"));
				}
				while (rs.next()) {
					drugs.put(drugDAO.findById(rs.getInt("drug_id")), rs.getInt("amount"));
				}
				order.setDrugs(drugs);
				orders.add(order);

			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find orders by user");
			e.printStackTrace();
		}

		logger.info("found orders by user");
		return orders;

	}
	
	@Override
	public Integer getTotalCount() {
		
		int count = 0;
		String query = "select count(distinct id) from mydb.orders";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all orders count");
			e.printStackTrace();
		}
		logger.info("found all orders count");
		return count;
		
	}

}
