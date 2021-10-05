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
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class Drugs implements RunCommand {

	private static Drugs instance = new Drugs();
	private static final Logger logger = LoggerFactory.getLogger(Drugs.class);
	private String actionTime;
	private List<Drug> drugs;
	private int totalCount;
	private Map<String, String[]> params;
	private int pageSize;
	private int currentPage;
	private User user;
	private Map<Integer, Date> drugsFromRecipe;

	private Drugs() {
		drugs = new ArrayList<Drug>();
		drugsFromRecipe = new HashMap<Integer, Date>();
	}
	
	public static Drugs getInstance() {
		return instance;
	}

	public int getTotalCount() {
		return totalCount;
	}
	
	public Map<Integer, Date> getDrugsFromRecipe() {
		return drugsFromRecipe;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public String getActionTime() {
		return actionTime;
	}

	public String getView() {
		return "drugs1.jsp";
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	@Override      
	public String run() {

		logger.info("hello from Drugs!");
		DrugManagerService service = DrugManagerService.getInstance();
		totalCount = service.getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		drugs = service.getDrugs(pageSize * (currentPage - 1), pageSize);
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

}
