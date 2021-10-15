package com.epam.jwd.apotheca.controller.action;

import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.model.User;


public class PrescribedRecipes implements RunCommand {

	private static PrescribedRecipes instance = new PrescribedRecipes();
	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private List<Map<String, String>> recipeInfo;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;

	private PrescribedRecipes() {
	}
	
	public static PrescribedRecipes getInstance() {
		return instance;
	}
	
	public String getView() {
		return "secure/prescribedRecipes1.jsp";
	}
	
	@Override
	public String run() {
			
		totalCount = RecipeManagerService.getInstance().getCountByDoctor(user);
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		
		recipeInfo = RecipeManagerService.getInstance().findByRange(user, pageSize * (currentPage - 1), pageSize);

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

	@Override
	public boolean isSecure() {
		
		return true;
	}
	
	public int getPagesCount() {
		return pagesCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public List<Map<String, String>> getRecipeInfo() {
		return recipeInfo;
	}
	
}
