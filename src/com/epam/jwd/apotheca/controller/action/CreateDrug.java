package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.validator.AccessValidator;
import com.epam.jwd.apotheca.controller.validator.DateValidator;
import com.epam.jwd.apotheca.controller.validator.DoctorValidator;
import com.epam.jwd.apotheca.controller.validator.DoseValidator;
import com.epam.jwd.apotheca.controller.validator.DrugNameValidator;
import com.epam.jwd.apotheca.controller.validator.DrugValidator;
import com.epam.jwd.apotheca.controller.validator.PriceValidator;
import com.epam.jwd.apotheca.controller.validator.QuantityValidator;
import com.epam.jwd.apotheca.controller.validator.UserValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class CreateDrug implements RunCommand {

	private static CreateDrug instance = new CreateDrug();
	private static final Logger logger = LoggerFactory.getLogger(CreateDrug.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	private boolean success;
	private List<String> errorMessages;
	private List<Validator> validators;
	
	private CreateDrug() {
		errorMessages = new ArrayList<String>();
		validators = new ArrayList<Validator>();
		validators.add(new DrugNameValidator(params));
		validators.add(new QuantityValidator(params));
		validators.add(new DoseValidator(params));
		validators.add(new PriceValidator(params));
	}
	
	public static CreateDrug getInstance() {
		return instance;
	}
	
	private void clearFields() {
		setSuccess(false);
		errorMessages.clear();
	}

	@Override
	public String run() {
		
		clearFields();
		
		if ( ! params.isEmpty() ) {
			for (Validator validator : validators) {
				validator.setValue(params);
				if ( ! validator.validate() ) {
					errorMessages.addAll( validator.getMessages() );
				}
			}
		}
		
		if (errorMessages.isEmpty()) {
			String drugName = params.get("drugName") == null ? null : params.get("drugName")[0];
			Integer quantity = (params.get("quantity") == null || params.get("quantity")[0].equals("")) ? null : Integer.valueOf(params.get("quantity")[0]);
			Integer price = (params.get("price") == null || params.get("price")[0].equals("")) ? null : Integer.valueOf(params.get("price")[0]);
			Double dose =  (params.get("dose") == null || params.get("dose")[0].equals("")) ? null : Double.valueOf( params.get("dose")[0]);
			Boolean prescription =  params.get("prescription") == null ? Boolean.FALSE : Boolean.TRUE;
	
			if (drugName != null && quantity != null && price != null && dose != null) {
				Drug drug = new Drug();
				drug.setName(drugName);
				drug.setQuantity(quantity);
				drug.setPrice(price);
				drug.setDose(dose);
				drug.setPrescription(prescription);
				if (DrugManagerService.getInstance().addDrug(drug)){
					setSuccess(true);
				}
			}
		}
		
		return actionTime;
		
	}

	@Override
	public String getView() {
		return "secure/createDrug1.jsp";
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

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

}
