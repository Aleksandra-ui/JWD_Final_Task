package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.ControllerFilter;
import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class DrugValidator implements Validator {

	private List<Drug> drugs;
	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(DrugValidator.class);
	
	public DrugValidator(List<Drug> drugs) {
		this.drugs = drugs;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		messages.clear();
		
		boolean result = true; 
		
		for ( Drug drug : drugs ) {
			if ( DrugManagerService.getInstance().getDrug(drug.getId()) == null ) {
				logger.info("a drug with id " + drug.getId() + " does not exist.");
				if (result) {
					result = false;
				}
			}
		}
		if (!result) {
			messages.add("Not all drugs in the list exist.");
		}
		
		return result;
		
	}

	public List<String> getMessages() {
		return messages;
	}

	@Override
	public void setValue(Object value) {
		drugs = (List<Drug>)value;
	}
	
	

}
