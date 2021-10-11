package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public abstract class RecipeCartAction implements RunCommand, RecipeCartAware {

	private RecipeCart cart;
	private Integer pageSize;
	private Integer currentPage;
	private Map<String, String[]> params;
	private List<Drug> drugs;
	private List<User> clients;
	private User user;
	
	public RecipeCartAction() {
		clients = new ArrayList<User>();
	}
	
	public Integer getTotalCount() {
		
		return cart.getDrugs().size();
	}
	
	
	@Override
	public String run() {
		
		UserManagerService userService = UserManagerService.getInstance();
		setClients( userService.getClients() );
		
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		
		setDrugs( getCart().getDrugs(getPageSize() * (getCurrentPage() - 1), getPageSize()) );
		
		return null;
		
	}
	
	@Override
	public String getView() {
		return "secure/recipeCart.jsp";
	}

	public Integer getPageSize() {
		// TODO Auto-generated method stub
		return pageSize;
	}

	
	public Integer getCurrentPage() {
		// TODO Auto-generated method stub
		return currentPage;
	}

	
	public RecipeCart getCart() {
		return cart;
	}
	
	
	public void setParams(Map<String, String[]> params) {
		this.params=params;
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	public void setDrugs(List<Drug> drugs) {
		this.drugs = drugs;
	}

	protected Map<String, String[]> getParams() {
		
		return params;
	}
	
	@Override
	public void setCart(RecipeCart cart) {
		this.cart = cart;
	}

	public List<User> getClients() {
		return clients;
	}

	public void setClients(List<User> clients) {
		this.clients.addAll(clients);
	}
	
	@Override
	public User getUser() {
		return user;
	}
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean isSecure() {
		return true;
	}
	
}
