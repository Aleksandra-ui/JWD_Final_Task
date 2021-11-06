package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.RecipeCartValidator;
import com.epam.jwd.apotheca.controller.validator.RoleAccessValidator;
import com.epam.jwd.apotheca.controller.validator.RoleNameValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public abstract class RecipeCartAction implements RunCommand, RecipeCartAware {

	private RecipeCart cart;
	private Integer pageSize;
	private Integer currentPage;
	private int pagesCount;
	private Map<String, String[]> params;
	private List<Drug> drugs;
	private List<User> clients;
	private User user;
	private Map<String, Validator> validators;
	protected List<Drug> invalidDrugs;
	private Map<String, String> errors;
	
	public RecipeCartAction() {
		invalidDrugs = new ArrayList<Drug>();
		errors = new HashMap<String, String>();
		clients = new ArrayList<User>();
		validators = new HashMap<String, Validator>();
		validators.put( "cart", new RecipeCartValidator() );
		validators.put( "doctor", new RoleAccessValidator(UserDAO.ROLE_NAME_DOCTOR) );
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
		pagesCount = getTotalCount() / pageSize + ((getTotalCount() % pageSize) == 0 ? 0 : 1);
		
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
		this.clients = clients;
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

	protected void updateCart() {
		
		invalidDrugs.clear();
		errors.clear();
		getCart().setInvalid(false);
		
		List<Drug> drugsToDisplay = new ArrayList<Drug>();
		if ( UserDAO.ROLE_NAME_DOCTOR.equalsIgnoreCase(user.getRole().getName()) ) {
			
			drugsToDisplay = getCart().getDrugs(getPageSize() * (getCurrentPage() - 1), getPageSize());
			
			setValidators();
			for ( Validator v : getValidators().values() ) {
				if ( ! v.validate() ) {
					getCart().setInvalid(true);
				}
			}
			
			if ( getCart().isInvalid() ) {
				
				//TODO 2.ecли дата истечения  рецепта вышла за пределы допустимого интервала
				//TODO 3.существует ли польз-тель в системе
				Date currentDate = new Date(System.currentTimeMillis());
	
				if ( getCart().getExpieryDate() != null ) {
					if ( currentDate.after(getCart().getExpieryDate()) ) {
						errors.put("date", "expiery date has expired");
					}
				} 
				if ( getCart().getUserId() == null ) {
					if ( params.get("clientId") != null ) {
						errors.put("user", "no such user in the system");
					}
				} else {
					if ( UserManagerService.getInstance().getUser(getCart().getUserId()) == null ) {
						getCart().setUserId(null);
						errors.put("user", "no such user in the system");
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
		
		} else {
			
			errors.put("access", user.getRole().getName());
			
		}
		
		setDrugs( drugsToDisplay );
		
	}

	public void setValidators() {
		getValidators().get("cart").setValue(getCart());
		getValidators().get("doctor").setValue(user);
	}

	public List<Drug> getInvalidDrugs() {
		return invalidDrugs;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public Map<String, Validator> getValidators() {
		return validators;
	}
	
	public  Map<String, String> getMonthNames() {

		Map<String, String> sortedMonthNames = new TreeMap<String, String>();
		Map<String, Integer> monthNames = GregorianCalendar.getInstance().getDisplayNames(Calendar.MONTH, Calendar.LONG, new Locale("en", "US"));
		for ( Map.Entry<String, Integer> e : monthNames.entrySet() ) {
			int v = e.getValue() + 1;
			sortedMonthNames.put( v < 10 ? ("0" + v) : String.valueOf(v),
					              e.getKey().toLowerCase() );
		}
		return sortedMonthNames;
		
	}

	public int getPagesCount() {
		return pagesCount;
	}
	
}
