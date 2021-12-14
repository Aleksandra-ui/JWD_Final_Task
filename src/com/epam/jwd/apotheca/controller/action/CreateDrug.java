package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.validator.DoseValidator;
import com.epam.jwd.apotheca.controller.validator.DrugNameValidator;
import com.epam.jwd.apotheca.controller.validator.PriceValidator;
import com.epam.jwd.apotheca.controller.validator.QuantityValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class CreateDrug implements RunCommand {

	private static final String name = "CreateDrug";
	private static CreateDrug instance = new CreateDrug();
	private static final Logger logger = LoggerFactory.getLogger(CreateDrug.class);
	private Map<String, String[]> params;
	private User user;
	private boolean success;
	private List<String> errorMessages;
	private Map<String, Validator> validators;
	
	private CreateDrug() {
		errorMessages = new ArrayList<String>();
		validators = new HashMap<String, Validator>();
		validators.put("name", new DrugNameValidator("drugName"));
		validators.put("quantity", new QuantityValidator("quantity"));
		validators.put("dose", new DoseValidator("dose"));
		validators.put("price", new PriceValidator("price"));
	}
	
	public static CreateDrug getInstance() {
		return instance;
	}
	
	private void clearFields() {
		setSuccess((params == null || params.isEmpty()) ? true : false);
		errorMessages.clear();
	}

	@Override
	public void run() {
		
		clearFields();
		
		if ( ! (params == null || params.isEmpty()) ) {
			
			validators.get("name").setValue(params);
			validators.get("quantity").setValue(params);
			validators.get("dose").setValue(params);
			validators.get("price").setValue(params);
			
			for (Validator validator : validators.values()) {
				if ( ! validator.validate() ) {
					errorMessages.addAll( validator.getMessages() );
					validator.getMessages().stream().forEach(m -> logger.error(m.toLowerCase()));
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
		}
			
	}

	@Override
	public String getView() {
		return "secure/createDrug.jsp";
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

	public String getName() {
		return name;
	}
	
}
