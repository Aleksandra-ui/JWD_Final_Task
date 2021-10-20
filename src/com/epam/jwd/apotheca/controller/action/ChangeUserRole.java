package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.AccessValidator;
import com.epam.jwd.apotheca.controller.validator.UserIdValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.User;

public class ChangeUserRole implements RunCommand {

	private static final Logger logger = LoggerFactory.getLogger(ChangeUserRole.class);
	private Map<String, String[]> params;
	private User user;
	private Map<String, Validator> validators;
	private Integer userId;
	private Integer roleId;
	private List<User> users;
	private List<String> messages;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	
	public ChangeUserRole() {	
		messages = new ArrayList<String>();
		validators = new HashMap<String, Validator>();
		validators.put("access", new AccessValidator("changeUserRole"));
		validators.put("user", new UserIdValidator("userId"));
	}

	@Override
	public String run() {
		
		messages.clear();
		

		validators.get("access").setValue(user);
		validators.get("user").setValue(params);
		for (Validator validator : validators.values()) {
			if ( ! validator.validate() ) {
				messages.addAll( validator.getMessages() );
			}
		}
		
		totalCount = UserManagerService.getInstance().getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		
		if (messages.isEmpty()) {
			
			userId = params.get("userId") == null ? null : Integer.valueOf(params.get("userId")[0]);
			roleId = params.get("roleId") == null ? null : Integer.valueOf(params.get("roleId")[0]);
			
			User result = UserManagerService.getInstance().changeRole(userId, roleId);
			if ( result != null ) {
				messages.add("User " + result.getName() + "'s role was changed to " + result.getRole().getName() + "."); 
				logger.info("user " + result.getName() + "'s role was changed to " + result.getRole().getName()); 
			} else {
				messages.add("Cannot change user's role."); 
				logger.error("error while attempting to change user's role"); 
			}
		
		}
		
		users = UserManagerService.getInstance().findUsersByRange(pageSize * (currentPage - 1), pageSize);
		
		return null;
	}

	@Override
	public String getView() {
		return "logonPage1.jsp";
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	public Integer getUserId() {
		return userId;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<String> getMessages() {
		return messages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public Integer getRoleId() {
		return roleId;
	}
	
}