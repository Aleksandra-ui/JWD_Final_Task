package com.epam.jwd.apotheca.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class RecipeDAOImpl implements RecipeDAO {

	private ConnectionPool cp = ConnectionPool.retrieve();

	public RecipeDAOImpl() {
		try {
			cp.init();
		} catch (CouldNotInitializeConnectionPoolException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Recipe> findAll() {
		return null;
	}

	@Override
	public List<Recipe> findAllById(Integer id) {
		return null;
	}

	@Override
	public Recipe update(Recipe entity) {
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		return false;
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
			e.printStackTrace();
		}

		return recipes;
	}

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
			e.printStackTrace();
		}

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
			e.printStackTrace();
		}

		return recipe;
	}

	public Integer getMaxId() {
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
			e.printStackTrace();
		}

		Recipe recipeInDB = null;
		if (result) {
			recipeInDB = findRecipe(id);
		}

		return recipeInDB;
	}

	public boolean deleteRecipe(Integer id, Integer userId, Integer drugId) {

		String query = "delete from mydb.recipe where id = ? and user_id = ? and drug_id = ?";
		try (Connection connection = cp.takeConnection(); PreparedStatement st = connection.prepareStatement(query);) {
			st.setInt(1, id);
			st.setInt(2, userId);
			st.setInt(3, drugId);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Recipe recipe = findRecipe(id);
		List<Integer> drugIds = recipe.getDrugIds();
		if (drugIds.contains(drugId)) {
			return false;
		} else {
			return true;
		}

	}

	public static void main(String[] args) {
		RecipeDAOImpl r = new RecipeDAOImpl();
		System.out.println(r.getMaxId());
	}

}
