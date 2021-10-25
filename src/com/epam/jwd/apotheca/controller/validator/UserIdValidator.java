package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.UserManagerService;
import java.util.Arrays;

public class UserIdValidator implements Validator {
	
	private static final Logger logger = LoggerFactory.getLogger(UserIdValidator.class);
	private List<String> messages;
	private String userIdParamName;
	private Map<String, String[]> params;

	public UserIdValidator(String userIdParamName) {
		this.userIdParamName = userIdParamName;
		messages = new ArrayList<String>();
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	@Override
	public boolean validate() {

		boolean result = true; 
		messages.clear();
		
		if ( params.get(userIdParamName) == null || params.get(userIdParamName)[0] == null ) {
			result = false;
			messages.add("User id isn't specified.");
			logger.warn("user id isn't specified");
		} else {
		
			try {
				Integer userId = Integer.valueOf( params.get(userIdParamName)[0] );
				if ( UserManagerService.getInstance().getUser(userId) == null ) {
					result = false;
					messages.add("User with id '" + userId + "' is not present in the system.");
					logger.warn("User with id '" + userId + "' is not present in the system.");
				}
			} catch (NumberFormatException e) {
				messages.add("User id value is wrong.");
				logger.warn("user id value is wrong");
				result = false;
				logger.error(Arrays.toString(e.getStackTrace()));
			}
			
		}
		
		return result;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		params = (Map<String, String[]>)value;
	}
}
