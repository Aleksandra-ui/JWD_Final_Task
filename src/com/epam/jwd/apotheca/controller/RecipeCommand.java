package com.epam.jwd.apotheca.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;


public class RecipeCommand implements RunCommand {

	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private int pageSize;
	private int currentPage;
	private List<Drug> drugs;
	private List<Drug> allPrescripted;
	private int totalCount;
	private List<User> clients;
	
	public RecipeCommand() {
		drugs = new ArrayList<Drug>();
	}
	
	public String getView() {
		return "secure/recipe1.jsp";
	}
	
	@Override
	public String run() {

		DrugManagerService drugService = DrugManagerService.getInstance();
		UserManagerService userService = new UserManagerService();
		
		totalCount = drugService.getPrescriptedCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		drugs = drugService.getPrescriptedDrugs(pageSize * (currentPage - 1), pageSize);
		allPrescripted = drugService.getPrescriptedDrugs();
		clients = userService.getClients();
		
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

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public List<User> getClients() {
		return clients;
	}

	public List<Drug> getAllPrescripted() {
		return allPrescripted;
	}

	@Override
	public boolean isSecure() {
		
		return true;
	}

}
