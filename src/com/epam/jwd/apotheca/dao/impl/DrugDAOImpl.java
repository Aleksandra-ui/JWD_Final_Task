package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.dao.ConnectionPool;
import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.model.Drug;

public class DrugDAOImpl implements DrugDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	
	@Override
	public Drug save(Drug entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Drug> findAll() {

		List<Drug> drugs = new ArrayList<Drug>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			connection.setAutoCommit(false);
			ResultSet rs = st.executeQuery("SELECT * FROM DRUGS" );
			while (rs.next()) {
				Drug drug = new Drug();
				drug.setName(rs.getString("name"));
				drug.setQuantity(rs.getInt("quantity"));
				drug.setPrice(rs.getInt("price"));
				drug.setId(rs.getInt("id"));
				drug.setDose(rs.getInt("dose"));
				drug.setPrescription(rs.getBoolean("prescription"));
				drugs.add(drug);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return drugs;
		
	}

	@Override
	public List<Drug> findAllById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drug update(Drug entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Drug findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
