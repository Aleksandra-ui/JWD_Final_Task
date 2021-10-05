package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityValidator implements Validator {

	private List<String> messages;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(QuantityValidator.class);
	
	public QuantityValidator(Map<String, String[]> params) {
		this.params = params;
		messages = new ArrayList<String>();
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	@Override
	public boolean validate() {

		boolean result = false;
		
		String quantity = params.get("quantity") == null ? null : params.get("quantity")[0];
		if ( quantity != null ) {
			try {
				Integer value = Integer.parseInt(quantity.trim());
				if ( value > 0 && value <= 1000 ) {
					result = true;
				} else {
					messages.add("Value '" + value + "' exceeds allowed range.");
					logger.error("Value '" + value + "' exceeds allowed range.");
				}
			} catch (NumberFormatException e) {
				messages.add("Value '" + quantity + "' is not a valid integer.");
				logger.error("Value '" + quantity + "' is not a valid integer.");
			}
		}
		
		return result;
		
	}
	
	public static void main(String[] args) {
		
		Map<String, String[]> map = new HashMap<>();
		map.put("quantity", new String[]{"0x109"});
		Validator drValidator = new QuantityValidator(map);
		System.out.println(drValidator.validate());
		
	}

}
