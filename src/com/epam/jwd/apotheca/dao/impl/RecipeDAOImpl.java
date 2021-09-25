package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class RecipeDAOImpl implements RecipeDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();
	private static final Logger logger = LoggerFactory.getLogger(RecipeDAOImpl.class);

	public RecipeDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}

		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query2);) {

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

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all recipes");
			e.printStackTrace();
		}

		logger.info("found all recipes");
		return recipes;
		
	}

	@Override
	public Recipe update(Recipe recipe) {
		
		boolean result = true;
		String query = "update mydb.recipe set expiery_date = ? where id = ? and drug_id = ? and user_id = ?";
		
		try (Connection connection = cp.takeConnection();
				PreparedStatement st = connection.prepareStatement(query)) {
			connection.setAutoCommit(false);
			st.setInt(2, recipe.getId());
			st.setInt(4, recipe.getUserId());
			st.setDate(1, recipe.getExpieryDate());
			
			for (Integer drugId : recipe.getDrugIds()) {
				st.setInt(3, drugId);
				if ( st.executeUpdate() <= 0 ) {
					result = false;
				}
				connection.commit();
			}			
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to update a recipe");
			e.printStackTrace();
		}
		
		logger.info("updated a recipe");
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
			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a recipe");
			e.printStackTrace();
		}

		logger.info("deleted a recipe");
		return result;
	
	}

	@Override
	public List<Recipe> findRecipe(User user) {

		String query = "select distinct r.id from mydb.recipe r"
				+ " join mydb.users u on u.id = r.user_id where u.id = ? or u.name = ? order by r.id";

		List<Recipe> recipes = new ArrayList<Recipe>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, user.getId());
			st.setString(2, user.getName());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				recipes.add(findRecipe(rs.getInt("id")));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find recipes by user");
			e.printStackTrace();
		}

		logger.info("found recipes by user");
		return recipes;
	}

	@Override
	public List<Recipe> findRecipeByDoctor(User doctor) {

		String query = "select distinct r.id from mydb.recipe r"
				+ " join mydb.users u on u.id = r.doctor_id where u.id = ? or u.name = ? order by r.id";

		List<Recipe> recipes = new ArrayList<Recipe>();

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, doctor.getId());
			st.setString(2, doctor.getName());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				recipes.add(findRecipe(rs.getInt("r.id")));
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find recipes by doctor");
			e.printStackTrace();
		}

		logger.info("found recipes by doctor");
		return recipes;
	}

	@Override
	public Recipe findRecipe(Integer id) {
		String query = "select r.id,r.user_id,r.doctor_id,r.drug_id,r.expiery_date from mydb.recipe r where r.id = ? order by r.id";
		Recipe recipe = null;

		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			List<Integer> drugIds = null;
			while (rs.next()) {
				if (recipe == null) {
					drugIds = new ArrayList<Integer>();
					recipe = new Recipe();
					recipe.setId(rs.getInt("id"));
					recipe.setUserId(rs.getInt("user_id"));
					recipe.setDoctorId(rs.getInt("doctor_id"));
					recipe.setExpieryDate(rs.getDate("expiery_date"));
				}

				drugIds.add(rs.getInt("drug_id"));
			}
			if (recipe != null) {
				recipe.setDrugIds(drugIds);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find a recipe");
			e.printStackTrace();
		}

		logger.info("found a recipe");
		return recipe;
	}

	private Integer getMaxId() {
		String query = "select max(id) from mydb.recipe";
		Integer id = null;
		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Recipe save(Recipe recipe) {
		boolean result = false;
		Integer id = getMaxId() + 1;

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {
			connection.setAutoCommit(false);
			for (Integer drugId : recipe.getDrugIds()) {
				result = st.executeUpdate("insert into mydb.recipe(id,user_id,drug_id,doctor_id,expiery_date) values ('"
						+ id + "','" + recipe.getUserId() + "','" + drugId + "','" + recipe.getDoctorId() + "','"
						+ recipe.getExpieryDate() + "')") > 0;
			}

			connection.commit();

		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to save a recipe");
			e.printStackTrace();
		}

		Recipe recipeInDB = null;
		if (result) {
			recipeInDB = findRecipe(id);
		}

		logger.info("saved a recipe");
		return recipeInDB;
	}

	@Override
	public boolean deleteRecipe(Integer id, Integer userId, Integer drugId) {

		String query = "delete from mydb.recipe where id = ? and user_id = ? and drug_id = ?";
		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, id);
			st.setInt(2, userId);
			st.setInt(3, drugId);
			st.executeUpdate();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to delete a recipe");
			e.printStackTrace();
		}

		Recipe recipe = findRecipe(id);
		List<Integer> drugIds = recipe.getDrugIds();
		if (drugIds.contains(drugId)) {
			logger.info("failed to delete a recipe");
			return false;
		} else {
			logger.info("deleted a recipe");
			return true;
		}

	}
	
	@Override
	public Integer getTotalCount() {
		
		int count = 0;
		String query = "select distinct count(id) from mydb.recipe";

		try (Connection connection = cp.takeConnection(); Statement st = connection.createStatement();) {

			ResultSet rs = st.executeQuery(query);
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			logger.error("catched SQL exception while attempting to find all recipe count");
			e.printStackTrace();
		}
		logger.info("found all recipe count");
		return count;
		
	}


}
