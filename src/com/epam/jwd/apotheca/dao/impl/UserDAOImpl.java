package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class UserDAOImpl implements UserDAO {
	
	private static UserDAOImpl instance = new UserDAOImpl();
	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	private UserDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}
	}
	
	public static UserDAOImpl getInstance() {
		return instance;
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

			logger.info("deleted a user");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a user");
			logger.error(Arrays.toString(e.getStackTrace()));
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
		String query = "update mydb.users set name = '" + entity.getName() + "', role_id = '" + entity.getRole().getId()
				+ "', password = '" + entity.getPassword() + "' where id = " + entity.getId();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			logger.trace("following query was executed successfully:\n" + query);
			if ( result ) {
				logger.info("updated a user");
				connection.commit();
			} else {
				logger.info("unable to update a user");
				connection.rollback();
			}
//			result = true;
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update a user");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return result ? entity : null;
	}

	public List<User> getUsers(Integer... id) {

		List<User> users = new ArrayList<User>();
		String query = "select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id " + (id.length > 0 ? "where u.id = " + id[0] : "")
				+ " order by u.id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(query);
			logger.trace("following query was executed successfully:\n" + query);
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
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
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
			logger.info("found a user by name");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a user by name");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return users.isEmpty() ? null : users.get(0);

	}

	public User createUser(String name, Integer role_id, String password) {

		boolean result = false;
		String query = "insert into mydb.users(name,role_id,password) values ('" + name + "'," + role_id
				+ ",'" + password + "')";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			if ( result ) {
				logger.info("created a user");
				connection.commit();
			} else {
				logger.info("unable to create a user");
				connection.rollback();
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to create a user");
			logger.error(Arrays.toString(e.getStackTrace()));
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
			
			logger.info("deleted a user");

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a user");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

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
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return result;

	}

	@Override
	public List<User> findUsersByRole(String roleName) {

		List<User> users = new ArrayList<User>();
		String query = "select u.id, u.name, u.password, r.id, r.name, r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id where r.name = '" + roleName + "' order by u.id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(query);
			logger.trace("following query was executed successfully:\n" + query);
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
			if ( users.isEmpty() ) {
				logger.info("no users with role '{}' were found", roleName);
			} else {
				logger.info("found {} users with role '{}'", users.size(), roleName);
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find users by role");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return users;
	}
	
	@Override
	public Integer getTotalCount() {
		return getTotalCount(false);
	}
	
	public Integer getTotalCount(boolean useSuperDoc) {
		
		int count = 0;
		String query = "select count(u.id) from mydb.users u " +
				(useSuperDoc ? "where u.name != 'super_doc'" : "");

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			logger.trace("following query was executed successfully:\n" + query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found all users count");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all users count");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return count;
		
	}

	public List<User> findByRange(Integer start, Integer count, boolean useSuperDoc) {

		List<User> users = new ArrayList<User>();
		String sql = "select u.id,u.name,u.password,r.id,r.name,r.permission from mydb.users u "
				+ "join mydb.roles r on r.id = u.role_id "
				+ (useSuperDoc ? "where u.name <> 'super_doc' " : "")
				+ "order by u.id asc limit " + start + "," + count;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery(sql);
			logger.trace("following query was executed successfully:\n" + sql);
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
			logger.info("found a range of users");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a range of users");
			logger.error("failed SQL:\n" + sql);
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return users;

	}
	
	public User changeRole(Integer userId, String roleName) {
		
		if ( userId == null || roleName == null ) {
			logger.warn("cannot change role. user or role isn't specified");
			return null;
		}
		
		User user = findById(userId);
		if ( user == null ) {
			logger.error("error while attempting to change role. user with id {} doesn't exist", userId);
			return null;
		}
		String userName = user.getName();
		
		if ( roleName.equals( user.getRole().getName() ) ) {
			logger.trace("the role is already assigned to user '{}'", user.getName());
			return user;
		}
		
		Role role = findRole(roleName);
		
		if ( role == null ) {
			logger.warn("such role doesn't exist");
			return null;
		}
		
		user.setRole(role);
		user = update(user);
		if ( user != null ) {
			logger.info("role of user {} was changed to '{}'", user.getName(), roleName);
		} else {
			logger.warn("cannot change the role of user '{}'", userName);
		}
		
		return user;
		
	}
	
	public Role findRole( String name ) {
		
		String query = "select r.name, r.permission, r.id from mydb.roles r where r.name = '" + name + "'";
		Role role = null;
		
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			ResultSet rs = st.executeQuery( query );
			logger.trace("following query was executed successfully:\n" + query);
			if (rs.next()) {
				role = new Role();
				role.setId(rs.getInt("r.id"));
				role.setPermission(rs.getInt("r.permission"));
				role.setName(name);
			}
			rs.close();
			if ( role == null ) {
				logger.warn("cannot find a role with name '" + name + "'");
			} else {
				logger.info("found a role with name '" + name + "'");
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a role with name '" + name + "'");
			logger.error("failed SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return role;
		
	}

}
