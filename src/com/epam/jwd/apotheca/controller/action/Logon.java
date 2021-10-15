package com.epam.jwd.apotheca.controller.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Recipe;
import com.epam.jwd.apotheca.model.Role;
import com.epam.jwd.apotheca.model.User;

public class Logon implements RunCommand {

	private static Logon instance = new Logon();
	private static final Logger logger = LoggerFactory.getLogger(Logon.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	private List<User> users;
	private List<String> messages;
	
	private Logon() {
		messages = new ArrayList<String>();
	}
	
	public static Logon getInstance() {
		return instance;
	}

	public String getActionTime() {
		return actionTime;
	}

	public String getView() {
		return "logonPage1.jsp";
	}
	
	@Override      
	public String run() {
		
		messages.clear();
		
		UserManagerService userService = UserManagerService.getInstance();
		String userName = params.get("name") != null ? params.get("name")[0] : null;
		String userPass = params.get("pass") != null ? params.get("pass")[0] : null;
		String userLogoff = params.get("logoff") != null ? params.get("logoff")[0] : null;
		String register = params.get("register") != null ? params.get("register")[0] : null;
		
		users = userService.getUsers();
		
		if ( user == null ) {
			if (  "1".equals(register) ){
				if (!userService.hasUser(userName)){
					user = new User();
					user.setName(userName);
					user.setPassword(userPass);
					Role role = new Role();
					role.setId(UserDAO.ROLE_CLIENT);
					role.setName("client");
					role.setPermission(UserDAO.PERM_CLIENT);
					user.setRole(role);
					if ( ! userService.createUser(user) ){
						user=null;
					}
				}
			} else {
				if ( userName != null && userService.hasUser(userName) ) {
					User user = userService.getUser(userName);
					if ( user.getPassword().equalsIgnoreCase(userPass) ) {
						this.user = user;
					}
				}
			}
		} if ("1".equals(userLogoff)) {
			user = null;
		}
		
		return actionTime;
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
		return false;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
