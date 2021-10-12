package com.epam.jwd.apotheca.controller;

import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.impl.RecipeDAOImpl;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class RecipeManagerService {

	private static RecipeManagerService instance = new RecipeManagerService();
	private RecipeDAO recipeDAO;

	private RecipeManagerService() {
		recipeDAO = RecipeDAOImpl.getInstance();
	}
	
	public static RecipeManagerService getInstance() {
		return instance;
	}

	public boolean addRecipe(Recipe recipe) {
		return recipeDAO.save(recipe) != null;
	}

	public List<Recipe> findByUser(User user) {

		return recipeDAO.findRecipe(user);

	}

	public List<Recipe> findByDoctor(User doctor) {

		return recipeDAO.findRecipeByDoctor(doctor);

	}
	
	public List<Map<String, String>> findByRange(User user, int start, int count) {
		
		return ((RecipeDAOImpl)recipeDAO).findRecipeInfoByRange(user, start, count);
		
	}
	
	public Integer getCountByDoctor(User doctor) {
		return ((RecipeDAOImpl)recipeDAO).getDrugsCountByDoctor(doctor);
	}

}
