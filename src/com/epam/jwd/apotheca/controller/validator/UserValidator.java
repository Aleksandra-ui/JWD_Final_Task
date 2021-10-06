package com.epam.jwd.apotheca.controller.validator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.epam.jwd.apotheca.controller.ControllerFilter;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;
import com.mysql.jdbc.Messages;

public class UserValidator implements Validator {

	private Map<String, String[]> params;
	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);
	
	public UserValidator(Map<String, String[]> params) {
		this.params = params;
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		messages.clear();
		
		String user = params.get("clientName")[0];
		
		if ( UserManagerService.getInstance().getUser(user) == null ) {
			result = false;
			messages.add("User " + user + " is not present in the system.");
			logger.warn("User " + user + " is not present in the system.");
		}
		
		return result;
		
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	@Override
	public void setValue(Object value) {
		params = (Map<String, String[]>)value;
	}
	
}
