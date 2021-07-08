package com.epam.jwd.apotheca.controller;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.dao.impl.RecipeDAOImpl;
import com.epam.jwd.apotheca.model.Recipe;

public class RecipeManagerService {
	
	private RecipeDAO recipeDAO;

	public RecipeManagerService() {
		recipeDAO = new RecipeDAOImpl();
	}
	
	public boolean addRecipe(Recipe recipe) {
		return recipeDAO.save(recipe) != null;
	}
	
}
