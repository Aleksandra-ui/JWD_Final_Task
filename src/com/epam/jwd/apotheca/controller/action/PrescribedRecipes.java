package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;


public class PrescribedRecipes implements RunCommand {

	private static PrescribedRecipes instance = new PrescribedRecipes();
	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private List<Recipe> recipes;//заменить на keySet()
	private Map<String, List<Drug>> drugsMap;
	private Map<String, User> userMap;

	private PrescribedRecipes() {
		recipes = new ArrayList<Recipe>();
		drugsMap = new HashMap<String, List<Drug>>();
		userMap = new HashMap<String, User>();
	}
	
	public static PrescribedRecipes getInstance() {
		return instance;
	}
	
	public String getView() {
		return "secure/prescribedRecipes1.jsp";
	}
	
	@Override
	public String run() {
		
		if ( params.get("doctor") != null ) {
			String doctorName = params.get("doctor")[0];
			User doctor = UserManagerService.getInstance().getUser(doctorName);
			recipes.addAll(RecipeManagerService.getInstance().findByDoctor(doctor));
			
			for ( Recipe recipe : recipes ) {
				userMap.put(String.valueOf(recipe.getId()), UserManagerService.getInstance().getUser(recipe.getUserId()));
				drugsMap.put(String.valueOf(recipe.getId()), new ArrayList<Drug>());
				for ( Integer drugId : recipe.getDrugIds() ) {
					drugsMap.get(String.valueOf(recipe.getId())).add(DrugManagerService.getInstance().getDrug(drugId));
				}
			}
		}
		
		return actionTime;
		
	}
	
	public String getActionTime() {
		return actionTime;
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public User getUser() {
		
		return user;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public Map<String, List<Drug>> getDrugsMap() {
		return drugsMap;
	}
	
	public Map<String, User> getUserMap() {
		return userMap;
	}

	@Override
	public boolean isSecure() {
		
		return true;
	}
	
}
