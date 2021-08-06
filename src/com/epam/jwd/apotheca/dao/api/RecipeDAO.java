package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public interface RecipeDAO extends DAO<Recipe> {
	
	List<Recipe> findRecipe(User user) ;
	
	Recipe findRecipe(Integer id) ;
	
	Recipe save(Recipe recipe);
	
	List<Recipe> findRecipeByDoctor(User doctor);
	
	boolean deleteRecipe(Integer id, Integer userId, Integer drugId);
	
}
