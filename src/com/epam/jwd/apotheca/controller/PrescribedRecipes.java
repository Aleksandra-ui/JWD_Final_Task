package com.epam.jwd.apotheca.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;


public class PrescribedRecipes implements RunCommand {

	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private List<Recipe> recipes;//заменить на keySet()
	private Map<Recipe, List<Drug>> drugsMap;
//	private int pageSize;
//	private int currentPage;
//	private List<Drug> drugs;
//	private List<Drug> allPrescripted;
//	private int totalCount;
//	private List<User> clients;
	
	public PrescribedRecipes() {
		recipes = new ArrayList<Recipe>();
		drugsMap = new HashMap<Recipe, List<Drug>>();
	}
	
	public String getView() {
		return "secure/prescribedRecipes1.jsp";
	}
	
	@Override
	public String run() {

//		DrugManagerService drugService = DrugManagerService.getInstance();
//		UserManagerService userService = new UserManagerService();
//		
//		totalCount = drugService.getPrescriptedCount();
//		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
//		currentPage = params.get("currentPage") == null ? 1
//				: Integer.valueOf(params.get("currentPage")[0]);
//		drugs = drugService.getPrescriptedDrugs(pageSize * (currentPage - 1), pageSize);
//		allPrescripted = drugService.getPrescriptedDrugs();
//		clients = userService.getClients();
		
		if ( params.get("doctor") != null ) {
			RecipeManagerService recipeService = new RecipeManagerService();
			UserManagerService userService = new UserManagerService();
			DrugManagerService drugService = DrugManagerService.getInstance();
			
			String doctorName = params.get("doctor")[0];
			User doctor = userService.getUser(doctorName);
			recipes.addAll(recipeService.findByDoctor(doctor));
			
			for ( Recipe recipe : recipes ) {
				drugsMap.put(recipe, new ArrayList<Drug>());
				for ( Integer drugId : recipe.getDrugIds() ) {
					drugsMap.get(recipe).add(drugService.getDrug(drugId));
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

	public Map<Recipe, List<Drug>> getDrugsMap() {
		return drugsMap;
	}

	@Override
	public boolean isSecure() {
		
		return true;
	}

//	public int getPageSize() {
//		return pageSize;
//	}
//
//	public int getCurrentPage() {
//		return currentPage;
//	}
//
//	public List<Drug> getDrugs() {
//		return drugs;
//	}
//
//	public int getTotalCount() {
//		return totalCount;
//	}
//
//	public List<User> getClients() {
//		return clients;
//	}
//
//	public List<Drug> getAllPrescripted() {
//		return allPrescripted;
//	}

	
}
