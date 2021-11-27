package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.pool.ConnectionPool;

import ch.qos.logback.classic.Level;

public class DrugDAOImpl implements DrugDAO {

	private static DrugDAOImpl instance = new DrugDAOImpl();
	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(DrugDAOImpl.class);
	
	private DrugDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.TRACE);
	}
	
	public static DrugDAOImpl getInstance() {
		return instance;
	}

	@Override
	public Drug save(Drug entity) {
		boolean result = false;
		String name = entity.getName();
		Integer quantity = entity.getQuantity();
		Integer price = entity.getPrice();
		Double dose = entity.getDose();
		Boolean prescription = entity.isPrescription();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO mydb.drugs(name,quantity,price,dose,prescription) VALUES ('" + name + "',"
					+ String.valueOf(quantity) + "," + String.valueOf(price) + "," + String.valueOf(dose) + ","
					+ (prescription ? "1" : "0") + ")";
			result = st.executeUpdate(sql) > 0;
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to save a drug");
			e.printStackTrace();
		}

		Drug drug = null;
		if (result) {
			drug = findDrug(name, dose);
		}

		logger.info("drug saved");
		return drug;
		
	}

	@Override
	public List<Drug> findAll() {

		List<Drug> drugs = new ArrayList<Drug>();
		String query = "select id,name,quantity,price,dose,prescription from mydb.drugs order by id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all drugs");
			e.printStackTrace();
		}
		logger.info("found all drugs");
		return drugs;

	}

	public List<Drug> findPrescripted() {

		List<Drug> drugs = new ArrayList<Drug>();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(
					"select id,name,quantity,price,dose,prescription from mydb.drugs where prescription = " + 1
							+ " order by id");
			while (rs.next()) {
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all prescripted drugs");
			e.printStackTrace();
		}
		logger.info("found all prescripted drugs");
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

	public List<Drug> findByRange(Integer start, Integer count, String columnName, boolean sortOrder) {
		
		List<Drug> drugs = new ArrayList<Drug>();

		columnName = columnName == null ? "id" : columnName;
		String sortColumns = ("id".equals(columnName)) ? ("id" + (sortOrder ? " desc" : " asc")) : columnName + (sortOrder ? " desc" : " asc") + ", id asc";
		
		String sql = "select id,name,quantity,price,dose,prescription from mydb.drugs order by " + sortColumns
				+ " limit ?,?";

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(sql);) {

			st.setInt(1, start);
			st.setInt(2, count);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {

				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a range of drugs");
			e.printStackTrace();
		}
		logger.info("found a range of drugs");
		return drugs;

	}

	public List<Drug> findPrescriptedByRange(Integer start, Integer end) {

		List<Drug> drugs = new ArrayList<Drug>();

		String sql = "select id,name,quantity,price,dose,prescription from mydb.drugs where prescription = " + 1
				+ " order by id asc limit ?,?";

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(sql);) {

			st.setInt(1, start);
			st.setInt(2, end);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {

				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a range of prescripted drugs");
			e.printStackTrace();
		}
		logger.info("found a range of prescripted drugs");
		return drugs;

	}

	@Override
	public Drug update(Drug entity) {
		boolean result = false;
		String query = "update mydb.drugs set quantity = " + entity.getQuantity() + ", price = "
				+ entity.getPrice() + ", prescription = " + (entity.isPrescription() ? "1" : "0") + " where id = "
				+ entity.getId();

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			if ( result ) {
				connection.commit();
				logger.info("updated a drug");
				logger.trace("following query was executed successfully:\n" + query);
			} else {
				connection.rollback();
				logger.trace("following query was executed with errors or warnings:\n" + query);
				logger.warn("transaction was rolled back");
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update a drug");
			logger.error("failed SQL:\n" + query);
			e.printStackTrace();
		}

		
		return result ? entity : null;
	}

	@Override
	public boolean delete(Integer id) {

		String query = "delete from mydb.drugs where id = " + id;
		boolean result = false;
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			result = st.executeUpdate(query) > 0;
			connection.commit();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a drug");
			e.printStackTrace();
		}

		logger.info("deleted a drug");
		return result;
	}

	@Override
	public Drug findById(Integer id) {

		List<Drug> drugs = findByIds(id);
		logger.info("found a drug by id");
		return drugs.size() == 0 ? null : drugs.get(0);
		
	}

	public List<Drug> findByIds(Integer... ids) {
		List<Drug> drugs = new ArrayList<Drug>();
		String idsStr = "";
		for (Integer id : ids) {
			idsStr += String.valueOf(id) + ",";
		}
		if (idsStr.length() != 0) {
			idsStr = idsStr.substring(0, idsStr.length() - 1);
			
			String query = "select id,name,quantity,price,dose,prescription from mydb.drugs where id "
					+ (ids.length == 1 ? " = ?" : "in (" + idsStr + ")");
			try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
				if (ids.length == 1) {
					st.setInt(1, ids[0]);
				}

				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					drugs.add(readDrug(rs));
				}
				rs.close();
				logger.info("found drugs by ids");
			} catch (SQLException e) {
				logger.error("catched SQL exception while attempting to find drugs by ids");
				e.printStackTrace();
			}
			return drugs;
		} else {
			return Collections.emptyList();
		}
		
	}

	public List<Drug> findByName(String name) {
		List<Drug> drugs = new ArrayList<Drug>();
		String query = "select id,name,quantity,price,dose,prescription from mydb.drugs WHERE NAME = '"
				+ name + "' order by id";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st
					.executeQuery(query);
			while (rs.next()) {
				drugs.add(readDrug(rs));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find drugs by name");
			e.printStackTrace();
		}

		logger.info("found drugs by name");
		return drugs;
	}

	@Override
	public Drug findDrug(String name, Double dose) {

		Drug drug = null;
		String query = "select id,name,quantity,price,dose,prescription from mydb.drugs where name = '" + name + "' and dose = " + dose + "";

		try (Connection connection = cp.takeConnection();
				Statement st = connection.createStatement()) {
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				drug = readDrug(rs);
			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a drug by name and dose");
			e.printStackTrace();
		}

		logger.info("found a drug by name and dose");
		return drug;

	}

	@Override
	public Integer getTotalCount() {
		
		int count = 0;
		String query = "select count(id) from mydb.drugs";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found all drugs count");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all drugs count");
			e.printStackTrace();
		}
		
		return count;
		
	}

	@Override
	public Integer getPrescriptedCount() {
		
		int count = 0;
		String query = "select count(id) from mydb.drugs where prescription = 1";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found prescripted drugs count");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find prescripted drugs count");
			e.printStackTrace();
		}
		
		return count;
		
	}
	
	public boolean changeQuantity(Drug drug, Integer amount) {
		
		Drug updatedDrug = new Drug();
		boolean result = true;
		int quantity = drug.getQuantity();
		int newQuantity = quantity - amount;
		
		if ( newQuantity < 0 ) {
			result = false;
		} else {
			updatedDrug.setQuantity(newQuantity);
			updatedDrug.setId(drug.getId());
			updatedDrug.setPrescription(drug.isPrescription());
			updatedDrug.setPrice(drug.getPrice());
			if ( update(updatedDrug) == null ) {
				result = false;
			}
		} 
		
		return result;
		
	}
	
	public List<Drug> sortByName(List<Drug> drugs, boolean asc) {
		
		List<Drug> sortedDrugs = new ArrayList<Drug>();
		List<Integer> ids = drugs.stream().map(d -> d.getId()).collect(Collectors.toList());
		
		String idsStr = "";
		for (Integer id : ids) {
			idsStr += String.valueOf(id) + ",";
		}
		if (idsStr.length() != 0) {
			idsStr = idsStr.substring(0, idsStr.length() - 1);
			
			String query = "select id,name,quantity,price,dose,prescription from mydb.drugs where id "
					+ (ids.size() == 1 ? " = ?" : "in (" + idsStr + ") order by name " + ( asc ? "asc" : "desc"));
			try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
				if (ids.size() == 1) {
					st.setInt(1, ids.get(0));
				}

				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					System.out.println(readDrug(rs));
					sortedDrugs.add(readDrug(rs));
				}
				rs.close();
				logger.info("sorted drugs by name");
			} catch (SQLException e) {
				logger.error("catched SQL exception while attempting to sort drugs by name");
				e.printStackTrace();
			}
			return sortedDrugs;
		} else {
			return Collections.emptyList();
		}
		
	}

}
