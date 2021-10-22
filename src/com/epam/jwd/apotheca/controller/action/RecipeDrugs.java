package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;


public class RecipeDrugs implements RunCommand, RecipeCartAware {

	private static RecipeDrugs instance = new RecipeDrugs();
	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private int pageSize;
	private int currentPage;
	private List<Drug> drugs;
	private int totalCount;
	private List<User> clients;
	private RecipeCart cart;
	
	private RecipeDrugs() {
		drugs = new ArrayList<Drug>();
	}
	
	public static RecipeDrugs getInstance() {
		return instance;
	}
	
	public String getView() {
		return "secure/recipe1.jsp";
	}
	
	@Override
	public String run() {

		totalCount = DrugManagerService.getInstance().getPrescriptedCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		drugs = DrugManagerService.getInstance().getPrescriptedDrugs(pageSize * (currentPage - 1), pageSize);
		clients = UserManagerService.getInstance().getClients();
		
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

	@Override
	public boolean isSecure() {
		
		return true;
	}

	@Override
	public RecipeCart getCart() {
		return cart;
	}

	@Override
	public void setCart(RecipeCart cart) {
		this.cart = cart;
	}

}
