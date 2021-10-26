package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.UserManagerService;

public class UserValidator implements Validator {

	private Map<String, String[]> params;
	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);
	private String userParamName;
	
	public UserValidator(String userParamName) {
		this.userParamName = userParamName;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		messages.clear();
		
		if ( params.get(userParamName) == null || params.get(userParamName)[0] == null || "".equals(params.get(userParamName)[0]) ) {
			result = false;
			messages.add("User is not specified.");
			logger.warn("user is not specified.");
		} else {
			String user = params.get(userParamName)[0];
			
			if ( UserManagerService.getInstance().getUser(user) == null ) {
				result = false;
				messages.add("User " + user + " is not present in the system.");
				logger.warn("User " + user + " is not present in the system.");
			}
		}
		
		return result;
		
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		params = (Map<String, String[]>)value;
	}
	
}
