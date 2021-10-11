package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.User;

public class DoctorValidator implements Validator, UserAware {

	private User user;
	private List<String> messages;
	
	public DoctorValidator(User user) {
		this.user = user;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		messages.clear();
		boolean result = true; 
		
		if ( user == null || user.getRole().getId() != 1 ) {
			messages.add("You have to be a doctor to prescribe recipes.");
			result = false;
		}
		
		return result;
		
	}

	public List<String> getMessages() {
		return messages;
	}
	
	@Override
	public void setValue(Object value) {
		user = (User)value;
	}

}
