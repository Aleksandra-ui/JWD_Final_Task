package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceValidator implements Validator {

	private List<String> messages;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(PriceValidator.class);
	
	public PriceValidator(Map<String, String[]> params) {
		this.params = params;
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
		
		String price = params.get("price") == null ? null : params.get("price")[0];
		if ( price != null ) {
			
			try {
				Integer value = Integer.parseInt(price.trim());
				if ( value > 0 ) {
					result = true;
				} else {
					messages.add("Value '" + value + "' exceeds allowed range.");
					logger.error("Value '" + value + "' exceeds allowed range.");
				}
			} catch (NumberFormatException e) {
				messages.add("Value '" + price + "' is not a valid integer.");
				logger.error("Value '" + price + "' is not a valid integer.");
			}
			
		}
		
		return result;
		
	}
	
	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}

}
