package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;
import com.epam.jwd.apotheca.pool.CouldNotInitializeConnectionPoolException;

public class UserDAOImpl implements UserDAO {

	public UserDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public User save(User entity) {
		return createUser(entity) ;
	}

	@Override
	public boolean delete(Integer id) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();PreparedStatement st = connection.prepareStatement("delete from mydb.users where id = ?");) {
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
		return  getUsers();
	}

	@Override
	public List<User> findAllById(Integer id) {
		return getUsers(id);
	}
	
	@Override
	public User findById(Integer id) {
		
		List<User> users = getUsers(id);
		return users.isEmpty() ? null : users.get(0);
		
	}

	@Override
	public User update(User entity) {
		boolean result = false;

		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(
					"update mydb.users set name = '" + entity.getName() + "', role = '" + entity.getRole()
							+ "', password = '" + entity.getPassword() + "' where id = " + entity.getId()) > 0;
			connection.commit();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result ? entity : null;
	}

	private ConnectionPool cp = ConnectionPool.retrieve();

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
				Statement st = conn.createStatement();) {
			conn.setAutoCommit(false);
			// Statement st = conn.createStatement();
			System.out
					.println(st.executeUpdate("INSERT INTO mydb.users(name,role,password) VALUES ('a','CLIENT','a')"));
			System.out.println(st.executeUpdate("update mydb.users set name = 'b' where id = 2"));
			System.out.println(st.executeUpdate("delete from mydb.users where id = 3;"));
			ResultSet rs = st.executeQuery("SELECT * FROM USERS WHERE ROLE = 'CLIENT'");
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setId(rs.getInt("id"));
				System.out.println(user);
				/*
				 * System.out.print(rs.getString("name") + " ");
				 * System.out.println(rs.getInt("id"));
				 */
			}
			conn.commit();
			conn.rollback();
			rs.close();
			/*
			 * st.close(); conn.close();
			 */

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("helloworld");
	}

	public List<User> getUsers(Integer...id) {

		List<User> users = new ArrayList<User>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st.executeQuery("SELECT * FROM USERS " + (id.length > 0 ? "WHERE ID = " + id[0] : ""));
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setId(rs.getInt("id"));
				users.add(user);
			}
			connection.commit();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;

	}
	
	public List<User> getUsers(String name) {

		List<User> users = new ArrayList<User>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st.executeQuery("SELECT * FROM USERS WHERE NAME = '" + name + "'");
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setId(rs.getInt("id"));
				users.add(user);
			}
			connection.commit();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;

	}

	public User createUser(User user) {

		return createUser(user.getName(), user.getRole(), user.getPassword());

	}

	public User createUser(String name, String role, String password) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate("INSERT INTO mydb.users(name,role,password) VALUES ('" + name + "','" + role
					+ "','" + password + "')") > 0;
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = null;
		if (result) {
			user = getUsers(name).get(0);
		}
		return user;

	}

	public boolean deleteUser(String name) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();PreparedStatement st = connection.prepareStatement("delete from mydb.users where name = ?");) {
			connection.setAutoCommit(false);
			st.setString(1, name);

			result = st.executeUpdate() > 0;
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public boolean hasUser(String name) {

		boolean result = false;

		try (Connection connection = cp.takeConnection();PreparedStatement st = connection.prepareStatement("select * from mydb.users where name = ?");) {
			connection.setAutoCommit(false);
			st.setString(1, name);

			ResultSet rs = st.executeQuery();
			result = rs.next();
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}
	
	public List<User> findUsersByRole(String role) {
		
		List<User> users = new ArrayList<User>();
		String query = "select id,name,role,password from mydb.users where role = ?";

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setString(1, role);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setPassword(rs.getString("password"));
				user.setId(rs.getInt("id"));
				users.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}


}
