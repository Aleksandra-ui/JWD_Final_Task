package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class DrugDAOImpl implements DrugDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	
	@Override
	public Drug save(Drug entity) {
		boolean result = false;
		String name = entity.getName();
		Integer quantity = entity.getQuantity();
		Integer price = entity.getPrice();
		Double dose = entity.getDose();
		Boolean prescription = entity.isPrescription();
		
		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO mydb.drugs(name,quantity,price,dose,prescription) VALUES ('" + name + "'," + String.valueOf(quantity) + "," + String.valueOf(price)
			+ "," + String.valueOf(dose) + "," + (prescription ? "1" : "0") + ")";
			System.out.println(sql);
			result = st.executeUpdate(sql) > 0;
			connection.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Drug drug = null;
		if (result) {
			drug = findDrug(name,dose);
		}
		
		return drug;
	}

	@Override
	public List<Drug> findAll() {

		List<Drug> drugs = new ArrayList<Drug>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery("select id,name,quantity,price,dose,prescription from mydb.drugs order by id" );
			while (rs.next()) {
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drugs;
		
	}

	private Drug readDrug(ResultSet rs) throws SQLException {
		Drug drug = new Drug();
		drug.setName(rs.getString("name"));
		drug.setQuantity(rs.getInt("quantity"));
		drug.setPrice(rs.getInt("price"));
		drug.setId(rs.getInt("id"));
		drug.setDose(rs.getDouble("dose"));
		drug.setPrescription(rs.getBoolean("prescription"));
		return drug;
	}

	@Override
	public List<Drug> findAllById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Drug> findById(Integer start, Integer end) {

		List<Drug> drugs = new ArrayList<Drug>();
		
		String sql = "select id,name,quantity,price,dose,prescription from mydb.drugs order by id asc limit ?,?";
		//"SELECT * FROM DRUGS WHERE ID BETWEEN ? AND ?"

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(sql);) {

			st.setInt(1, start);
			st.setInt(2, end);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drugs;
		
	}

	@Override
	public Drug update(Drug entity) {
		boolean result = false;

		try (Connection connection = cp.takeConnection();Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(
					"update mydb.drugs set quantity = " + entity.getQuantity()
							+ ", price = " + entity.getPrice() + ", prescription = " + (entity.isPrescription() ? "1" : "0") + " where id = " + entity.getId()) > 0;
			connection.commit();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result ? entity : null;
	}

	@Override
	public boolean delete(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Drug findById(Integer id) {
		Drug drug = null;
		String query = "select id,name,quantity,price,dose,prescription from mydb.drugs where id = ?";
		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			drug = readDrug(rs);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drug;
	}
	
	public List<Drug> findByName(String name) {
		List<Drug> drugs = new ArrayList<Drug>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery("select id,name,quantity,price,dose,prescription from mydb.drugs WHERE NAME = '" + name + "' order by id");
			while (rs.next()) {
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return drugs;
	}
	
	public Drug findDrug(String name, Double dose) {

		Drug drug = null;

		try (Connection connection = cp.takeConnection();PreparedStatement st = connection.prepareStatement("select id,name,quantity,price,dose,prescription from mydb.drugs where name = ? and dose = ?");) {
			st.setString(1, name);
			st.setDouble(2, dose);
			ResultSet rs = st.executeQuery();
			if ( rs.next() ) {
				drug = readDrug(rs);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return drug;

	}

}
