package com.epam.jwd.apotheca.controller;

import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.RecipeDAOImpl;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class RecipeManagerService {

	private static RecipeManagerService instance = new RecipeManagerService();
	private RecipeDAO recipeDAO;
	private UserDAO userDAO;

	private RecipeManagerService() {
		recipeDAO = RecipeDAOImpl.getInstance();
		userDAO = UserDAOImpl.getInstance();
	}
	
	public static RecipeManagerService getInstance() {
		return instance;
	}

	public boolean addRecipe(Recipe recipe) {
		return recipeDAO.save(recipe) != null;
	}

	public List<Recipe> findByUser(User user) {

		return recipeDAO.findRecipes(user);

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

	public boolean deleteUserRecipes(User user) {
		boolean result = true;
		List<Recipe> recipes = ((RecipeDAOImpl)recipeDAO).findRecipes(user);
		for ( Recipe recipe : recipes ) {
			result &= ((RecipeDAOImpl)recipeDAO).delete(recipe.getId());
		}
		return result;
	}
	
	public boolean switchToSuperDoc(User doctor) {

		boolean result = true;
		if ( doctor != null ) {
			
			if ( ! UserDAO.ROLE_NAME_SUPER_DOC.equals(doctor.getName()) ) {
				
				List<Recipe> recipes = recipeDAO.findRecipeByDoctor(doctor);
				User superDoc = userDAO.getUser(UserDAO.ROLE_NAME_SUPER_DOC);
				for ( Recipe recipe : recipes ) {
					recipe.setDoctorId(superDoc.getId());
					result &= recipeDAO.update(recipe) != null;
				}
				
			}
			
		}
		return result;

	}
	
}
