package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class AccessValidator implements Validator, UserAware {

	private String action;
	private User user;
	private List<String> messages;
	
	public AccessValidator(String action) {
		this.action = action;
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
		} else if ( "changeUserRole".equals(action) ) {
			UserManagerService userService = UserManagerService.getInstance();
			if ( ! userService.isRoleEnabled(user, UserDAO.PERM_ADMIN) ) {
				messages.add("You are not allowed to change users roles");
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
