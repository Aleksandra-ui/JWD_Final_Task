package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class Drugs implements RunCommand, ShoppingCartAware {

	private static Drugs instance = new Drugs();
	private static final Logger logger = LoggerFactory.getLogger(Drugs.class);
	private static final String name = "Drugs";
	private String actionTime;
	private List<Drug> drugs;
	private Map<String, String[]> params;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private User user;
	private Map<Integer, Date> drugsFromRecipe;
	private ShoppingCart cart;
	
	private Drugs() {
		drugs = new ArrayList<Drug>();
		drugsFromRecipe = new HashMap<Integer, Date>();
	}
	
	public static Drugs getInstance() {
		return instance;
	}

	public Map<Integer, Date> getDrugsFromRecipe() {
		return drugsFromRecipe;
	}

	public String getActionTime() {
		return actionTime;
	}

	public String getView() {
		return "drugs.jsp";
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	@Override      
	public String run() {
		
		drugsFromRecipe.clear();

		String sortColumn = params.get("sortColumn") == null ? null : params.get("sortColumn")[0];
		boolean sortOrder = params.get("sortOrder") == null ? false : Boolean.valueOf(params.get("sortOrder")[0]);
		
		totalCount = DrugManagerService.getInstance().getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		int startIndex =  pageSize * (currentPage - 1);
		int count =  currentPage < pagesCount ? pageSize :totalCount % pageSize ;
		drugs = DrugManagerService.getInstance().getDrugs(startIndex, count, sortColumn, sortOrder);
		
		actionTime = GregorianCalendar.getInstance().getTime().toString();
		
		RecipeManagerService recipeService = RecipeManagerService.getInstance();
		if (user != null) {
			List<Recipe> recipesForUser = recipeService.findByUser(user);
			for (Recipe recipe : recipesForUser) {
				Date expieryDate = recipe.getExpieryDate();
				for (Integer drugId : recipe.getDrugIds()) {
					drugsFromRecipe.put(drugId, expieryDate);
				}
			}
		}
		logger.trace("displaying " + count + " drugs starting from " + startIndex);
		
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
		
		return false;
	}

	@Override
	public ShoppingCart getCart() {
		return cart;
	}

	@Override
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
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
	
	public String getName() {
		return name;
	}
	
}
