package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugNameValidator implements Validator {

	private List<String> messages;
	private Map<String, String[]> params;
	private static final Logger logger = LoggerFactory.getLogger(DrugNameValidator.class);
	
	public DrugNameValidator(Map<String, String[]> params) {
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
		
		String drugName = params.get("drugName") == null ? null : params.get("drugName")[0];
		if ( drugName != null ) {
			if ( Pattern.matches("^\\s*[\\dA-Za-zА-Яа-я,;'\"\\s]+", drugName) ) {
				result = true;
			}
		}
		
		if (!result) {
			logger.error("'" + drugName + "' is not a valid drug name.");
			messages.add("'" + drugName + "' is not a valid drug name.");
		}
		
		return result;
		
	}
	
	public static void main(String[] args) {
		
		Map<String, String[]> map = new HashMap<>();
		map.put("drugName", new String[]{" "});
		Validator drValidator = new DrugNameValidator(map);
		System.out.println(drValidator.validate());
		
	}

	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}

}