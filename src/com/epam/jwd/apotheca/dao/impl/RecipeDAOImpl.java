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

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class RecipeDAOImpl implements RecipeDAO {

	private static RecipeDAOImpl instance = new RecipeDAOImpl();
	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(RecipeDAOImpl.class);

	private RecipeDAOImpl() {
		if ( ! cp.getInitialized().get() ) {
			try {
				cp.init();
			} catch (CouldNotInitializeConnectionPoolException e) {
				logger.error(Arrays.toString( e.getStackTrace() ));
			}
		}
	}
	
	public static RecipeDAOImpl getInstance() {
		return instance;
	}

	@Override
	public List<Recipe> findAll() {
		
		List<Recipe> recipes = new ArrayList<Recipe>();
		Set<Integer> ids = new HashSet<Integer>();
		String query1 = "select id from mydb.recipe";
		String query2 = "select user_id, drug_id, doctor_id, expiery_date from mydb.recipe where id = ?";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {

			ResultSet rs = st.executeQuery(query1);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all recipes");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query2)) {

			for (Integer id : ids) {
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				Recipe recipe = new Recipe();
				List<Integer> drugIds = new ArrayList<>();
				recipe.setId(id);
				if (rs.next()) {
					recipe.setUserId(rs.getInt("user_id"));
					recipe.setExpieryDate(rs.getDate("expiery_date"));
					recipe.setDoctorId(rs.getInt("doctor_id"));
					drugIds.add(rs.getInt("drug_id"));
				}
				while (rs.next()) {
					drugIds.add(rs.getInt("drug_id"));
				}
				recipe.setDrugIds(drugIds);
				recipes.add(recipe);

			}
			logger.info("found all recipes");

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all recipes");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return recipes;
		
	}

	@Override
	public Recipe update(Recipe recipe) {
		
		boolean result = true;
		String baseQuery = "update mydb.recipe set expiery_date = '" + 
						recipe.getExpieryDate() + "', doctor_id = "
						+ recipe.getDoctorId() + " where id = " + recipe.getId() 
						+ " and user_id = " + recipe.getUserId();
		String query = "";
		
		try (Connection connection = cp.takeConnection();
				Statement st = connection.createStatement()) {
			
			connection.setAutoCommit(false);
			for (Integer drugId : recipe.getDrugIds()) {
				query = baseQuery + " and drug_id = " + drugId;
				if ( st.executeUpdate(query) <= 0 ) {
					result = false;
					logger.trace("following query was executed with errors or warnings:\n" + query);
				} else {
					logger.trace("following query was executed successfully:\n" + query);
				}
			}	
			if ( result ) {
				connection.commit();
				logger.info("updated a recipe with id " + recipe.getId());
			} else {
				connection.rollback();
				logger.warn("unable to update a recipe with id " + recipe.getId());
			}
			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update a recipe with id " + recipe.getId());
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return result ? recipe : null;
		
	}

	@Override
	public boolean delete(Integer id) {
		
		boolean result = false;
		String query = "delete from mydb.recipe where id = ?";

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query)) {
			
			connection.setAutoCommit(false);
			st.setInt(1, id);
			result = st.executeUpdate() > 0;
			if (result) {
				logger.info("deleted a recipe");
			} else {
				logger.warn("recipe can't be deleted");
			}
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a recipe");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return result;
	
	}

	@Override
	public List<Recipe> findRecipes(User user) {

		String query = "select distinct r.id from mydb.recipe r"
				+ " join mydb.users u on u.id = r.user_id where u.id = ? or u.name = ? order by r.id";

		List<Recipe> recipes = new ArrayList<Recipe>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query)) {
			st.setInt(1, user.getId());
			st.setString(2, user.getName());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				recipes.add(findRecipe(rs.getInt("id")));
			}
			rs.close();
			logger.info("found recipes by user");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find recipes by user");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return recipes;
	}

	@Override
	public List<Recipe> findRecipeByDoctor(User doctor) {

		String query = "select distinct r.id from mydb.recipe r"
				+ " join mydb.users u on u.id = r.doctor_id where u.id = ? or u.name = ? order by r.id";

		List<Recipe> recipes = new ArrayList<Recipe>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query)) {
			st.setInt(1, doctor.getId());
			st.setString(2, doctor.getName());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				recipes.add(findRecipe(rs.getInt("r.id")));
			}
			rs.close();
			logger.info("found recipes by doctor");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find recipes by doctor");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return recipes;
		
	}

	@Override
	public Recipe findRecipe(Integer id) {
		
		String query = "select r.user_id,r.doctor_id,r.drug_id,r.expiery_date from mydb.recipe r where r.id = " + id;
		Recipe recipe = null;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {
			
			ResultSet rs = st.executeQuery(query);
			List<Integer> drugIds = null;
			while (rs.next()) {
				if (recipe == null) {
					drugIds = new ArrayList<Integer>();
					recipe = new Recipe();
					recipe.setId(id);
					recipe.setUserId(rs.getInt("user_id"));
					recipe.setDoctorId(rs.getInt("doctor_id"));
					recipe.setExpieryDate(rs.getDate("expiery_date"));
				}

				drugIds.add(rs.getInt("drug_id"));
			}
			if (recipe != null) {
				recipe.setDrugIds(drugIds);
				logger.info("found a recipe");
			} else {
				logger.info("recipe with specified id doesn't exist");
			}
			rs.close();
			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a recipe");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return recipe;
		
	}

	private Integer getMaxId() {
		
		String query = "select max(id) from mydb.recipe";
		Integer id = null;
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt(1);

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to assign a recipe id");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		return id;
		
	}

	@Override
	public Recipe save(Recipe recipe) {
		
		boolean result = true;
		Integer id = getMaxId() + 1;
		String query = "";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {
			
			connection.setAutoCommit(false);
			for (Integer drugId : recipe.getDrugIds()) {
				query = "insert into mydb.recipe(id,user_id,drug_id,doctor_id,expiery_date) values ('"
						+ id + "','" + recipe.getUserId() + "','" + drugId + "','" + recipe.getDoctorId() + "','"
						+ recipe.getExpieryDate() + "')";
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
				logger.info("saved a recipe");
			} else {
				connection.rollback();
				logger.warn("transaction was rolled back");
			}
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to save a recipe");
			logger.error("failure during handling an SQL:\n" + query);
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		Recipe recipeInDB = null;
		if (result) {
			recipeInDB = findRecipe(id);
		}

		return recipeInDB;
		
	}

	@Override
	public boolean deleteRecipe(Integer id, Integer userId, Integer drugId) {

		String query = "delete from mydb.recipe where id = ? and user_id = ? and drug_id = ?";
		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query)) {
			st.setInt(1, id);
			st.setInt(2, userId);
			st.setInt(3, drugId);
			connection.setAutoCommit(false);
			st.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a recipe");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		Recipe recipe = findRecipe(id);
		
		if ( recipe != null ) {
			List<Integer> drugIds = recipe.getDrugIds();
			if (drugIds.contains(drugId)) {
				logger.warn("failed to delete a recipe");
				return false;
			}
		}
		
		logger.info("deleted a recipe");
		return true;

	}
	
	@Override
	public Integer getTotalCount() {
		
		int count = 0;
		String query = "select count(distinct id) from mydb.recipe";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found all recipe count");
			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all recipe count");
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		return count;
		
	}
	
	public Integer getDrugsCountByDoctor(User doctor) {
		
		int count = 0;
		String query = "select count(r.id) from mydb.recipe r "
				+ "join mydb.users u on r.doctor_id = u.id "
				+ "where u.id = " + doctor.getId();
		
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement()) {
			
			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
			logger.info("found count of drugs prescripted by doctor " + doctor.getName());
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find count of drugs prescripted by doctor " + doctor.getName());
			logger.error(Arrays.toString(e.getStackTrace()));
		}
		
		return count;
		
	}
	
	public List<Map<String, String>> findRecipeInfoByRange(User user, int start, int count) {

		String query = "select r.id, u.name, d.name, d.dose, r.expiery_date from mydb.recipe r "
				+ "join mydb.users doc on r.doctor_id = doc.id "
				+ "join mydb.users u on r.user_id = u.id "
				+ "join mydb.drugs d on r.drug_id = d.id "
				+ "where r.doctor_id = ? "
				+ "order by r.id desc, r.expiery_date desc, d.id asc "
				+ "limit ?,?;";
			
		List<Map<String, String>> recipes = new ArrayList<Map<String, String>>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query)) {
			st.setInt(1, user.getId());
			st.setInt(2, start);
			st.setInt(3, count);
			ResultSet rs = st.executeQuery();
			Map<String, String> item;
			while (rs.next()) {
				item = new HashMap<String, String>();
				item.put("id", String.valueOf(rs.getInt("r.id")));
				item.put("name", rs.getString("u.name"));
				item.put("drug", rs.getString("d.name"));
				item.put("dose", String.valueOf(rs.getDouble("d.dose")));
				item.put("date", String.valueOf(rs.getDate("r.expiery_date")));
				recipes.add(item);
			}
			rs.close();
			logger.info("found recipes by doctor");
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find recipes by doctor");
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		return recipes;
		
	}

}
