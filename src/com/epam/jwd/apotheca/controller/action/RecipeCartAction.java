package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.RecipeCartValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
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
	private Validator validator;
	protected List<Drug> invalidDrugs;
	private Map<String, String> errors;
	
	public RecipeCartAction() {
		invalidDrugs = new ArrayList<Drug>();
		errors = new HashMap<String, String>();
		clients = new ArrayList<User>();
		validator = new RecipeCartValidator();
	}
	
	public Integer getTotalCount() {
		
		return cart.getDrugs().size();
	}
	
	
	@Override
	public String run() {
		
		UserManagerService userService = UserManagerService.getInstance();
		setClients( userService.getClients() );
		getCart().setExpieryDate(null);
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		
		return null;
		
	}
	
	@Override
	public String getView() {
		return "secure/recipeCart.jsp";
	}

	public Integer getPageSize() {
		return pageSize;
	}

	
	public Integer getCurrentPage() {
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

	public Validator getValidator() {
		return validator;
	}

	protected void updateDrugs() {
		
		invalidDrugs.clear();
		errors.clear();
		getCart().setInvalid(false);
		
		List<Drug> drugsToDisplay = getCart().getDrugs(getPageSize() * (getCurrentPage() - 1), getPageSize());
		
		getValidator().setValue(getCart());
		if ( ! getValidator().validate() ) {
			getCart().setInvalid(true);
		}
		
		if ( getCart().isInvalid() ) {
			
			
			
			//TODO 2.ecли дата истечения  рецепта вышла за пределы допустимого интервала
			//TODO 3.существует ли польз-тель в системе
			Date currentDate = new Date(System.currentTimeMillis());

			if ( ! ( getCart().getExpieryDate() == null && UserManagerService.getInstance().getUser(getCart().getUserId()) == null )  ) {
				if ( UserManagerService.getInstance().getUser(getCart().getUserId()) == null ) {
					errors.put("user", "no such user is present in the system");
				}
				if ( currentDate.after(getCart().getExpieryDate()) ) {
					errors.put("date", "expiery date has expired");
				}
			} 
			
			for ( Drug d : getCart().getDrugs() ) {
				Drug actualDrug = DrugManagerService.getInstance().getDrug(d.getId());
				//TODO 1.ecли лек-во было по рецепту,а стало в свободном доступе
				if ( ! actualDrug.isPrescription() ) {
					invalidDrugs.add(d);
				}
			}
			
		}
		
		setDrugs( drugsToDisplay );
		
	}

	public List<Drug> getInvalidDrugs() {
		return invalidDrugs;
	}

	public Map<String, String> getErrors() {
		return errors;
	}
	
}
