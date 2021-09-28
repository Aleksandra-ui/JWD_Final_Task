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
		recipeDAO = new RecipeDAOImpl();
		userDAO = new UserDAOImpl();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionPool.retrieve().destroy();
	}


	@Test
	public void testFindRecipe() {
		Recipe recipeInDB;
		Recipe found;
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
		recipeInDB = recipeDAO.save(recipe);
		Integer id = recipeInDB.getId();
		recipe.setId(id);
		found = recipeDAO.findRecipe(id);
		Assert.assertEquals(recipe,found);
		
		recipeDAO.delete(id);
	}

	@Test
	public void testSave() {
		Recipe recipeInDB;
		Recipe recipe = new Recipe();
		recipe.setDoctorId(12);
		recipe.setUserId(1);
		List<Integer> drugIds = new ArrayList<>();
		drugIds.add(1);
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
		recipeInDB = recipeDAO.save(recipe);
		if ( recipeInDB != null ) {
			Integer id = recipeInDB.getId();
			recipe.setId(id);
			Assert.assertEquals ( recipe, recipeInDB );
		}
		
		recipeDAO.delete(recipeInDB.getId());
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
		
		Recipe recipe = new Recipe();
		recipe.setDoctorId(12);
		recipe.setUserId(1);
		List<Integer> drugIds = new ArrayList<>();
		drugIds.add(1);
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
		recipe = recipeDAO.save(recipe);
		
		int second = recipeDAO.getTotalCount();
		
		userDAO.delete(recipe.getId());
		
		assert first == second - 1;
		
	}

}
