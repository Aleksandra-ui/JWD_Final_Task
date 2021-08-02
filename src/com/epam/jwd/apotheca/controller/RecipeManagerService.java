package com.epam.jwd.apotheca.controller;

import java.util.List;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.impl.RecipeDAOImpl;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class RecipeManagerService {

	private RecipeDAO recipeDAO;

	public RecipeManagerService() {
		recipeDAO = new RecipeDAOImpl();
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

}
