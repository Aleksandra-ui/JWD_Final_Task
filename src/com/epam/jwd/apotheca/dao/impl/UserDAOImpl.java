package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class UserDAOImpl implements UserDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	
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

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement("delete from mydb.users where id = ?");) {
			connection.setAutoCommit(false);
			st.setInt(1, id);

			result = st.executeUpdate() > 0;
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

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

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(
					"update mydb.users set name = '" + entity.getName() + "', role_id = '" + entity.getRole().getId()
							+ "', password = '" + entity.getPassword() + "' where id = " + entity.getId()) > 0;
			connection.commit();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result ? entity : null;
	}

	public List<User> getUsers(Integer... id) {

		List<User> users = new ArrayList<User>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st
					.executeQuery("select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
							+ "join mydb.roles r on r.id = u.role_id " + (id.length > 0 ? "where u.id = " + id[0] : "")
							+ " order by u.id");
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
			connection.commit();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;

	}

	public User getUser(String name) {

		List<User> users = new ArrayList<User>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st
					.executeQuery("select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
							+ "join mydb.roles r on r.id = u.role_id " + "where u.name = '" + name + "' order by u.id");
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
			connection.commit();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users.isEmpty() ? null : users.get(0);

	}

	public User createUser(String name, Integer role_id, String password) {

		boolean result = false;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate("insert into mydb.users(name,role_id,password) values ('" + name + "'," + role_id
					+ ",'" + password + "')") > 0;
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		User user = null;
		if (result) {
			user = getUser(name);
		}
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
			e.printStackTrace();
		}

		return result;

	}

	public boolean hasUser(String name) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement("select * from mydb.users where name = ?");) {
			connection.setAutoCommit(false);
			st.setString(1, name);

			ResultSet rs = st.executeQuery();
			result = rs.next();
			connection.commit();

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
			e.printStackTrace();
		}

		return users;
	}

}
