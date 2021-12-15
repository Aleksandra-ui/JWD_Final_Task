package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityValidator implements Validator {

	private List<String> messages;
	private Map<String, String[]> params;
	private String quantityParamName;
	private static final Logger logger = LoggerFactory.getLogger(QuantityValidator.class);
	
	public QuantityValidator(String quantityParamName) {
		this.quantityParamName = quantityParamName;
		messages = new ArrayList<String>();
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	@Override
	public boolean validate() {
		
		messages.clear();
		boolean result = false;
		
		String quantity = params.get(quantityParamName) == null ? null : params.get(quantityParamName)[0];
		if ( quantity != null ) {
			try {
				Integer value = Integer.parseInt(quantity.trim());
				if ( value > 0 && value <= 1000 ) {
					result = true;
				} else {
					messages.add("Value '" + value + "' exceeds allowed range.");
					logger.error("value '" + value + "' exceeds allowed range");
				}
			} catch (NumberFormatException e) {
				messages.add("Value '" + quantity + "' is not a valid integer.");
				logger.error("value '" + quantity + "' is not a valid integer");
			}
		}
		
		return result;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}

}
