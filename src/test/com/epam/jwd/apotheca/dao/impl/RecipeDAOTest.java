package com.epam.jwd.apotheca.dao.impl;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.api.RecipeDAO;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.dao.impl.RecipeDAOImpl;
import com.epam.jwd.apotheca.dao.impl.UserDAOImpl;
import com.epam.jwd.apotheca.model.Recipe;
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
		Assert.assertEquals(recipe,found);
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    java.util.Date utilDate = null;
		try {
			utilDate = format.parse("2022/12/03");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Date sqlDate = new Date(utilDate.getTime());
		recipe.setExpieryDate(sqlDate);
		recipeInDB = recipeDAO.save(recipe);
		if ( recipeInDB != null ) {
			Integer id = recipeInDB.getId();
			recipe.setId(id);
			System.out.println(recipe);
			System.out.println(recipeInDB);
			Assert.assertEquals ( recipe.getExpieryDate(),(recipeInDB.getExpieryDate()));
		}
	}
	
	@Test
	public void testDeleteRecipe() {
		Recipe recipe = new Recipe();
		recipe.setDoctorId(4);
		recipe.setUserId(13);
		List<Integer> drugIds = new ArrayList<>();
		drugIds.add(6);
		drugIds.add(14);
		recipe.setDrugIds(drugIds);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    java.util.Date utilDate = null;
		try {
			utilDate = format.parse("2021/09/10");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Date sqlDate = new Date(utilDate.getTime());
		recipe.setExpieryDate(sqlDate);
		Integer id = recipeDAO.save(recipe).getId();
		
		((RecipeDAOImpl)recipeDAO).deleteRecipe(id, 13, 6);
		Recipe recipeInDB = recipeDAO.findRecipe(id);
		List<Integer> drugIds2 = recipeInDB.getDrugIds();
		assert ! (drugIds2.contains(6));
	}
	
	@Test
	public void testFindRecipesByDoctor() {
		
		User doctor = ((UserDAOImpl)userDAO).getUser("p");
		List<Recipe> recipes = recipeDAO.findRecipeByDoctor(doctor);
		System.out.println(recipes);
		Assert.assertNotNull(recipes);
		
	}

}
