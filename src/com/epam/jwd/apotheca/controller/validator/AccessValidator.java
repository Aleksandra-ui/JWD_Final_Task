package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.User;

public class AccessValidator implements Validator, UserAware {

	private String action;
	private User user;
	private List<String> messages;
	
	public AccessValidator(String action, User user) {
		this.action = action;
		this.user = user;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true; 
		messages.clear();
		if ( "createRecipe".equals(action) ) {
			UserManagerService userService = UserManagerService.getInstance();
			if ( ! userService.canPrescribe(user) ) {
				messages.add("You are not allowed to view this page");
				result = false;
			}
		}
		
		return result;
		
	}

	public List<String> getMessages() {
		return messages;
	}

	@Override
	public void setValue(Object value) {
		this.user = (User)value;
	}

}
