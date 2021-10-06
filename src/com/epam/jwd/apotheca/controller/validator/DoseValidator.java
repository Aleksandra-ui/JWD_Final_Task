package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoseValidator implements Validator {

	private List<String> messages;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(DoseValidator.class);
	
	public DoseValidator(Map<String, String[]> params) {
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
		
		String dose = params.get("dose") == null ? null : params.get("dose")[0];
		if ( dose != null ) {
			if ( Pattern.matches("(\\d+\\.?\\d+|\\d+)$", dose.trim()) ) {
				try {
					Float value = Float.parseFloat(dose.trim());
					if ( value > 0 && value <= 1000 ) {
						result = true;
					} else {
						messages.add("Value '" + value + "' exceeds allowed range.");
						logger.error("Value '" + value + "' exceeds allowed range.");
					}
				} catch (NumberFormatException e) {
					messages.add("Value '" + dose + "' is not a valid decimal.");
					logger.error("Value '" + dose + "' is not a valid decimal.");
				}
			} else {
				messages.add("Value '" + dose + "' is not valid.");
				logger.error("Value '" + dose + "' is not valid.");
			}
		}
		
		return result;
		
	}

	
	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}

}
