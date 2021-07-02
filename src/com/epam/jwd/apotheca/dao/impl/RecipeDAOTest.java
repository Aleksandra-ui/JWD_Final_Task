package com.epam.jwd.apotheca.dao.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.pool.ConnectionPool;

public class RecipeDAOTest {

	private static RecipeDAO recipeDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		recipeDAO = new RecipeDAOImpl();
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
		recipe.setDoctorId(12);
		recipe.setUserId(2);
		List<Integer> drugIds = new ArrayList<>();
		drugIds.add(16);
		
		recipe.setDrugIds(drugIds);
		Date date = new Date(876767678);
		recipe.setExpieryDate(date);
		recipeInDB = recipeDAO.save(recipe);
		Integer id = recipeInDB.getId();
		recipe.setId(id);
		found = recipeDAO.findRecipe(id);
		assertEquals(recipe,found);
	}
	
	@Test
	public void testFindRecipes() {
		
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
		Date date = new Date(8789798);
		recipe.setExpieryDate(date);
		recipeInDB = recipeDAO.save(recipe);
		if ( recipeInDB != null ) {
			Integer id = recipeInDB.getId();
			recipe.setId(id);
			System.out.println(recipe);
			System.out.println(recipeInDB);
			assertEquals ( recipe.getExpieryDate(),(recipeInDB.getExpieryDate()));
		}
	}

}
