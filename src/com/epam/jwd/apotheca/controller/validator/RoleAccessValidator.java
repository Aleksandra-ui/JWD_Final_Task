package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.epam.jwd.apotheca.model.User;

public class RoleAccessValidator implements Validator, UserAware {

	private String[] roles;
	private User user;
	private List<String> messages;
	
	public RoleAccessValidator(String... roles) {
		this.roles = roles;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = false; 
		messages.clear();
		
		if ( roles.length == 0 ) {
			result = true;
		} else {
		
			for ( String role : roles ) {
				if ( role.equals(user.getRole().getName()) ) {
					result = true;
					break;
				}
			}
			
		}
		
		if ( ! result ) {
			messages.add("User has no access rights.");
			messages.add("User has role '" + user.getRole().getName() + "', but roles " + Arrays.toString(roles) + " are expected.");
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
