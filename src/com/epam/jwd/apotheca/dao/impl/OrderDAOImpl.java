package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class OrderDAOImpl implements OrderDAO {

	private static OrderDAOImpl instance = new OrderDAOImpl();
	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

	private OrderDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
	}
	
	public static OrderDAOImpl getInstance() {
		return instance;
	}

	@Override
	public Order save(Order order) {

		boolean result = true;
		Integer id = getMaxId() + 1;
		Map<Drug, Integer> drugs = order.getDrugs();
		String query = "";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			for (Drug drug : drugs.keySet()) {
				query = "insert into mydb.orders (id, drug_id, amount, user_id, order_date)"
						+ "values ('" + id + "','" + drug.getId() + "','" + drugs.get(drug) + "','" + order.getUserId()
						+ "','" + order.getDate() + "')";
				boolean localResult = st.executeUpdate(query) > 0;
				if ( localResult ) {
					logger.trace("following query was executed successfully:\n" + query);
				} else {
					logger.trace("following query was executed with errors or warnings:\n" + query);
				}
				result &= localResult;
			}
			if (result) {
				connection.commit();
				logger.info("saved an order");
				
			} else {
				connection.rollback();
				logger.warn("transaction was rolled back");
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to save an order");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		} catch (NullPointerException e) {
			logger.error("catched  exception while attempting to save an order");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		Order orderInDB = null;
		if (result) {
			orderInDB = findOrder(id);
		}

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
			logger.error(Arrays.toString(e.getStackTrace()));
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
			
			logger.info("find all orders");

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all orders");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
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
			logger.info("updated an order");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update an order");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return result ? order : null;
		
	}

	@Override
	public boolean delete(Integer id) {
		boolean result = false;
		String query = "delete from mydb.orders where id = " + id;

		try (Connection connection = cp.takeConnection();
				Statement st = connection.createStatement()) {
			connection.setAutoCommit(false);
			
			result = st.executeUpdate(query) > 0;
		
			if (result) {
				logger.trace("following query was executed successfully:\n" + query);
				connection.commit();
				logger.info("deleted an order");
			} else {
				logger.trace("following query was executed with errors or warnings:\n" + query);
				connection.rollback();
				logger.warn("order can't be deleted");
				logger.warn("transaction was rolled back");
			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete an order");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}

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
			logger.error(Arrays.toString(e.getStackTrace()));
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
			logger.trace("following query was executed successfully:\n" + query);
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

			logger.info("found an order by id");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find an order by id");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return order;

	}

	@Override
	public List<Order> findOrdersByUser(Integer userId) {

		List<Order> orders = new ArrayList<Order>();
		DrugDAO drugDAO = DrugDAOImpl.getInstance();
		String query = "select id, drug_id, amount, order_date from mydb.orders where user_id = " + userId
				+ " order by order_date desc, id desc, drug_id asc";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(query);
			Map<Drug, Integer> drugs = null;
			Order order = null;
			int orderId = -1;
			while (rs.next()) {
				if (orderId != rs.getInt("id")) {
					if (order != null) {
						order.setDrugs(drugs);
						orders.add(order);
					}
					drugs = new HashMap<Drug, Integer>();
					order = new Order();
					orderId = rs.getInt("id");
					order.setId(orderId);
					order.setUserId(userId);
					order.setDate(rs.getDate("order_date"));
				}

				drugs.put(drugDAO.findById(rs.getInt("drug_id")), rs.getInt("amount"));
			}
			if (order != null) {
				order.setDrugs(drugs);
				orders.add(order);
			}
			logger.info("found orders by user");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find orders by user");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

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
			logger.info("found all orders count");
			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all orders count");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		return count;
		
	}
	
	public Integer getDrugsCountByUser(Integer userId) {
		
		Integer count = 0;
		String query = "select count(d.id) from mydb.orders o join mydb.drugs d on o.drug_id = d.id where o.user_id = " + userId;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found all drugs count in user's orders");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all drugs count in user's orders");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return count;
		
	}
	
	public List<Map<String, String>> findDrugInfoByRange(User user, int start, int count) {

		String query = "select o.id, d.name, d.dose, o.amount, o.order_date from mydb.orders o "
				+ "join mydb.drugs d on o.drug_id = d.id "
				+ "where o.user_id = ? "
				+ "order by o.id desc, d.id asc "
				+ "limit ?,?;";
			
		List<Map<String, String>> drugsInfo = new ArrayList<Map<String, String>>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, user.getId());
			st.setInt(2, start);
			st.setInt(3, count);
			ResultSet rs = st.executeQuery();
			Map<String, String> item;
			while (rs.next()) {
				item = new HashMap<String, String>();
				item.put("id", String.valueOf(rs.getInt("o.id")));
				item.put("name", rs.getString("d.name"));
				item.put("dose", String.valueOf(rs.getDouble("d.dose")));
				item.put("amount", String.valueOf(rs.getDouble("o.amount")));
				item.put("date", String.valueOf(rs.getDate("o.order_date")));
				drugsInfo.add(item);
			}
			rs.close();
			logger.info("found all drugs info in user's orders");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all drugs info in user's orders");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return drugsInfo;
	}

}
