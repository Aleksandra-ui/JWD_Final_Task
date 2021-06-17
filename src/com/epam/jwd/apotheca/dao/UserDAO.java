package com.epam.jwd.apotheca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.jwd.apotheca.model.User;

public class UserDAO {

	public UserDAO() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ConnectionPool cp = ConnectionPool.retrieve();
	// private static Connection connection = null;

	/*
	 * static {
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver"); if (connection == null) {
	 * connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",
	 * "root", "root"); } } catch (SQLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } catch (ClassNotFoundException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); }
	 * 
	 * System.out.println("driver initialized");
	 * 
	 * }
	 */

	/*
	 * public void finalize() { try { connection.close();
	 * System.out.println("connection closed"); } catch (SQLException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
				Statement st = conn.createStatement();) {
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb?user=root&password=root");
			/*
			 * Properties props = new Properties(); props.put("user", "root");
			 * props.put("password", "root");
			 * DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", props);
			 */
			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root",
			// "root");
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

	public List<User> getUsers() {

		List<User> users = new ArrayList<User>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st.executeQuery("SELECT * FROM USERS");
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

	public boolean createUser(User user) {

		return createUser(user.getName(), user.getRole(), user.getPassword());

	}

	public boolean createUser(String name, String role, String password) {

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

		return result;

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

}
