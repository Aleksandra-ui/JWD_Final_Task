package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class UserDAOImpl implements UserDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	public UserDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User save(User user) {
		return createUser(user.getName(), user.getRole().getId(), user.getPassword());
	}

	@Override
	public boolean delete(Integer id) {

		boolean result = false;
		String query = "delete from mydb.users where id = ?";

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query);) {
			connection.setAutoCommit(false);
			st.setInt(1, id);

			result = st.executeUpdate() > 0;
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a user");
			e.printStackTrace();
		}

		logger.info("deleted a user");
		return result;

	}

	@Override
	public List<User> findAll() {
		return getUsers();
	}

	@Override
	public User findById(Integer id) {

		List<User> users = getUsers(id);
		return users.isEmpty() ? null : users.get(0);

	}

	@Override
	public User update(User entity) {
		boolean result = false;
		String query = "update mydb.users set name = '" + entity.getName() + "', role_id = '" + entity.getRole().getId()
				+ "', password = '" + entity.getPassword() + "' where id = " + entity.getId();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			connection.commit();
			result = true;
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update a user");
			e.printStackTrace();
		}

		logger.info("updated a user");
		return result ? entity : null;
	}

	public List<User> getUsers(Integer... id) {

		List<User> users = new ArrayList<User>();
		String query = "select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id " + (id.length > 0 ? "where u.id = " + id[0] : "")
				+ " order by u.id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.id"));
				role.setPermission(rs.getInt("r.permission"));
				role.setName(rs.getString("r.name"));
				user.setName(rs.getString("u.name"));
				user.setRole(role);
				user.setPassword(rs.getString("u.password"));
				user.setId(rs.getInt("u.id"));
				users.add(user);
			}
			rs.close();
			logger.info("found users");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find users");
			e.printStackTrace();
		}

		
		return users;

	}

	public User getUser(String name) {

		List<User> users = new ArrayList<User>();
		String query = "select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id " + "where u.name = '" + name + "' order by u.id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st
					.executeQuery(query);
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("r.id"));
				role.setPermission(rs.getInt("r.permission"));
				role.setName(rs.getString("r.name"));
				User user = new User();
				user.setName(rs.getString("u.name"));
				user.setRole(role);
				user.setPassword(rs.getString("u.password"));
				user.setId(rs.getInt("u.id"));
				users.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a user by name");
			e.printStackTrace();
		}

		logger.info("found a user by name");
		return users.isEmpty() ? null : users.get(0);

	}

	public User createUser(String name, Integer role_id, String password) {

		boolean result = false;
		String query = "insert into mydb.users(name,role_id,password) values ('" + name + "'," + role_id
				+ ",'" + password + "')";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to create a user");
			e.printStackTrace();
		}

		User user = null;
		if (result) {
			user = getUser(name);
		}
		logger.info("created a user");
		return user;

	}

	public boolean deleteUser(String name) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement("delete from mydb.users where name = ?");) {
			connection.setAutoCommit(false);
			st.setString(1, name);

			result = st.executeUpdate() > 0;
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a user");
			e.printStackTrace();
		}

		logger.info("deleted a user");
		return result;

	}

	public boolean hasUser(String name) {

		boolean result = false;
		String query = "select * from mydb.users where name = ?";

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query);) {
			st.setString(1, name);

			ResultSet rs = st.executeQuery();
			result = rs.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public List<User> findUsersByRole(Integer roleId) {

		List<User> users = new ArrayList<User>();
		String query = "select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id where r.id = ? order by u.id";

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, roleId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				User user = new User();
				Role role = new Role();
				role.setId(rs.getInt("r.id"));
				role.setPermission(rs.getInt("r.permission"));
				role.setName(rs.getString("r.name"));
				user.setName(rs.getString("u.name"));
				user.setRole(role);
				user.setPassword(rs.getString("u.password"));
				user.setId(rs.getInt("u.id"));
				users.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find users by role");
			e.printStackTrace();
		}

		logger.info("found users by role");
		return users;
	}
	
	@Override
	public Integer getTotalCount() {
		
		int count = 0;
		String query = "select count(id) from mydb.users";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all users count");
			e.printStackTrace();
		}
		logger.info("found all users count");
		return count;
		
	}


}
