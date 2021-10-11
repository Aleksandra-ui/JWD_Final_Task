package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.RecipeManagerService;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.AccessValidator;
import com.epam.jwd.apotheca.controller.validator.DateValidator;
import com.epam.jwd.apotheca.controller.validator.DoctorValidator;
import com.epam.jwd.apotheca.controller.validator.UserAware;
import com.epam.jwd.apotheca.controller.validator.UserValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.User;

public class CreateRecipe implements RunCommand, RecipeCartAware {

	private static CreateRecipe instance = new CreateRecipe();
	private List<Drug> drugs;
	private String actionTime;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(CreateRecipe.class);
	private String displayPage = "secure/createRecipe1.jsp";
	private List<String> errorMessages;
	private List<Validator> validators;
	private RecipeCart cart;
	private User user;
	private User client;
	private String expieryDate;

	private CreateRecipe() {
		drugs = new ArrayList<Drug>();
		params = new HashMap<String, String[]>();
		errorMessages = new ArrayList<String>();
		validators = new ArrayList<Validator>();
		validators.add(new DateValidator(params));
//		validators.add(new DrugValidator(drugs));
		validators.add(new UserValidator(params));
		validators.add(new AccessValidator("createRecipe", user));
		validators.add(new DoctorValidator(user));
	}
	
	public static CreateRecipe getInstance() {
		return instance;
	}

	private void clearFields() {
		drugs.clear();
		errorMessages.clear();
	}
	
	@Override
	public String run() {
		
		if ( getCart().getDrugs().isEmpty() ) {
			return null;
		}
		
		clearFields();
		
		for (Validator validator : validators) {
			if ( ! validator.validate() ) {
				errorMessages.addAll( validator.getMessages() );
			}
		}
		
		if (errorMessages.isEmpty()) {
			RecipeManagerService service = RecipeManagerService.getInstance();
			UserManagerService uService = UserManagerService.getInstance();
			Recipe recipe = new Recipe();
	
			Integer doctorId = getUser().getId();
			recipe.setDoctorId(doctorId);
			String clientName = params.get("clientName")[0];
			User client = uService.getUser(clientName);
			recipe.setUserId(client.getId());

			List<Integer> drugIds = getCart().getDrugs().stream().map(d -> d.getId()).collect(Collectors.toList());
			
	 		recipe.setDrugIds(drugIds);
	 		
			String expieryDate = params.get("year")[0] + "/" + params.get("month")[0] + "/" + params.get("day")[0];
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		    java.util.Date utilDate = null;
			try {
				utilDate = format.parse(expieryDate);
			} catch (ParseException e) {
				logger.error(Arrays.toString(e.getStackTrace()));
			}
		    Date sqlDate = new Date(utilDate.getTime());
			recipe.setExpieryDate(sqlDate);
			
			if ( service.addRecipe(recipe) ) {
				this.expieryDate = expieryDate;
				this.client = client;
				drugs.addAll( DrugManagerService.getInstance().getDrugs(drugIds.toArray(new Integer[0])) );
				getCart().clear();
			} else {
				errorMessages.add("Unable to create recipe.");
				logger.error("Unable to create recipe.");
			}
		
		} 
		return actionTime;
		
	}

	@Override
	public String getView() {
		
		return displayPage;
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params.putAll(params);
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		for ( Validator validator : validators ) {
			if ( validator instanceof UserAware ) {
				validator.setValue(this.user);
			}
		}
	}
	
	@Override
	public User getUser() {
		
		return user;
	}
	
	public String getActionTime() {
		return actionTime;
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
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

	public User getClient() {
		return client;
	}

	public String getExpieryDate() {
		return expieryDate;
	}
	
}
