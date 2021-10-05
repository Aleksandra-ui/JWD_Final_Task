package com.epam.jwd.apotheca.dao.impl;

import java.util.ArrayList;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class RecipeDAOTest {

	private static RecipeDAO recipeDAO;
	private static UserDAO userDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		recipeDAO = RecipeDAOImpl.getInstance();
		userDAO = UserDAOImpl.getInstance();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}


	@Test
	public void testFindRecipe() {
		
		Recipe recipeInDB;
		Recipe found;
		Recipe recipe = createRecipe();
		recipeInDB = recipeDAO.save(recipe);
		Integer id = recipeInDB.getId();
		recipe.setId(id);
		found = recipeDAO.findRecipe(id);
		
		recipeDAO.delete(id);
		
		Assert.assertEquals(recipe, found);
		
	}

	@Test
	public void testSave() {
		
		Recipe recipe = createRecipe();
		Recipe recipeInDB = recipeDAO.save(recipe);

		Integer id = recipeInDB.getId();
		recipe.setId(id);
		
		recipeDAO.delete(id);
		
		Assert.assertEquals ( recipe, recipeInDB );
		
	}
	
	@Test
	public void testFindRecipesByDoctor() {
		
		User doctor = ((UserDAOImpl)userDAO).getUser("Aleksandr Udin");
		List<Recipe> recipes = recipeDAO.findRecipeByDoctor(doctor);
		Assert.assertNotNull(recipes);
		
	}
	
	@Test
	public void testGetTotalCount() {
		
		int first = recipeDAO.getTotalCount();
		
		Recipe recipe = recipeDAO.save(createRecipe());
		
		int second = recipeDAO.getTotalCount();
		
		userDAO.delete(recipe.getId());
		
		assert first == second - 1;
		
	}
	
	@Test
	public void testDeleteRecipe() {
		
		Recipe recipe = recipeDAO.save(createRecipe());
		System.out.println("recipe from DB: " + recipe);
		
		//for ( int i = 0; i < recipe.getDrugIds().size(); i ++ ) {
			recipeDAO.deleteRecipe(recipe.getId(), recipe.getUserId(), recipe.getDrugIds().get(0));
		//}
		
		Assert.assertNull(recipeDAO.findRecipe(recipe.getId()));
		
	}
	
	public Recipe createRecipe() {
		
		Recipe recipe = new Recipe();
		recipe.setDoctorId(16);
		recipe.setUserId(20);
		
		List<Integer> drugIds = new ArrayList<>();
		drugIds.add(2);
		recipe.setDrugIds(drugIds);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    java.util.Date utilDate = null;
		try {
			utilDate = format.parse("2022/12/03");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    Date sqlDate = new Date(utilDate.getTime());
		recipe.setExpieryDate(sqlDate);
		
		return recipe;
		
	}

}
