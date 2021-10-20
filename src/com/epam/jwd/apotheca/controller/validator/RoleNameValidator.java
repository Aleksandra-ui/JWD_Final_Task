package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.UserManagerService;

public class RoleNameValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(UserIdValidator.class);
	private String roleParamName;
	private Map<String, String[]> params;
	private List<String> messages;
	
	public RoleNameValidator( String roleParamName ) {
		messages = new ArrayList<String>();
		this.roleParamName = roleParamName;
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	@Override
	public boolean validate() {
		
		boolean result = true; 
		messages.clear();
		
		if ( params == null || params.get(roleParamName) == null ) {
			result = false;
			messages.add("Role name isn't specified.");
			logger.warn("role name isn't specified");
		} else {
			String roleName = params.get(roleParamName)[0];
			if ( UserManagerService.getInstance().findRole(roleName) == null ) {
				result = false;
				messages.add("Role with name '" + roleName + "' doesn't exist.");
				logger.warn("role with name '" + roleName + "' doesn't exist");
			}
		}
		
		return result;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.params = (Map<String, String[]>)value;
	}

}
