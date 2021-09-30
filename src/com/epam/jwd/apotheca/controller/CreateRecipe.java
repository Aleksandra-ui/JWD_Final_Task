package com.epam.jwd.apotheca.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.validator.AccessValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;

import com.epam.jwd.apotheca.model.User;

public class CreateRecipe implements RunCommand {
	
	private List<Drug> drugs;
	private String doctorName;
	private User user;
	private String actionTime;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(CreateRecipe.class);
	private String displayPage = "createRecipe1.jsp";
	private List<String> errorMessages;
	private List<Validator> validators;

	public CreateRecipe() {
		drugs = new ArrayList<Drug>();
		errorMessages = new ArrayList<String>();
		validators = new ArrayList<Validator>();
		validators.add(new AccessValidator("createRecipe", user));
	}

	private void clearFields() {
		drugs.clear();
		errorMessages.clear();
	}
	
	@Override
	public String run() {
		
		clearFields();
		for (Validator validator : validators) {
			if ( ! validator.validate() ) {
				errorMessages.addAll( validator.getMessages() );
			}
		}
		
		if (errorMessages.isEmpty()) {
			RecipeManagerService service = new RecipeManagerService();
			UserManagerService uService = new UserManagerService();
			DrugManagerService dService = DrugManagerService.getInstance();
			
			Recipe recipe = new Recipe();
			List<Integer> drugIds = new ArrayList<Integer>();
			Integer userId = user.getId();
			recipe.setUserId(userId);
	 		String[] strings = params.get("recipeDrugIds")[0].split(",");
	 		for ( String drug : strings ){
	 			if ( ! drug.equals("") ) {
	 				drugIds.add(Integer.valueOf(drug));	
	 			}
	 		}
	 		recipe.setDrugIds(drugIds);
			recipe.setDoctorId(Integer.valueOf(params.get("doctorId")[0]));
			String expieryDate = params.get("year")[0] + "/" + params.get("month")[0] + "/" + params.get("day")[0];
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		    java.util.Date utilDate = null;
			try {
				utilDate = format.parse(expieryDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    Date sqlDate = new Date(utilDate.getTime());
			recipe.setExpieryDate(sqlDate);
			service.addRecipe(recipe);
			
			for ( Integer id : drugIds ) {
				Drug drug = dService.getDrug(id); 
				logger.info(drug.toString());
				drugs.add(drug);
			}
			
			User doctor = uService.getUser(Integer.valueOf(params.get("doctorId")[0]));
			doctorName = doctor.getName();
		} 
		return actionTime;
		
	}

	@Override
	public String getView() {
		
		return displayPage;
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getActionTime() {
		return actionTime;
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
}
