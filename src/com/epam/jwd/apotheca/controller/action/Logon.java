package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.dao.api.UserDAO;
import com.epam.jwd.apotheca.model.User;

public class Logon implements RunCommand {

	private static final String name = "Logon";
	private static Logon instance = new Logon();
	private static final Logger logger = LoggerFactory.getLogger(Logon.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	private List<User> users;
	private List<String> messages;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private HttpSession session;
	
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
		return "logonPage.jsp";
	}
	
	@Override      
	public String run() {
		
		messages.clear();
		UserManagerService userService = UserManagerService.getInstance();
		
		totalCount = userService.getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		users = userService.findUsersByRange(pageSize * (currentPage - 1), pageSize);
		
		String userName = params.get("name") != null ? params.get("name")[0] : null;
		String userPass = params.get("pass") != null ? params.get("pass")[0] : null;
		String userLogoff = params.get("logoff") != null ? params.get("logoff")[0] : null;
		String register = params.get("register") != null ? params.get("register")[0] : null;
		
		if ( user == null ) {
			if (  "1".equals(register) ){
				if (! userService.hasUser(userName)){
					user = new User();
					user.setName(userName);
					user.setPassword(userPass);
					user.setRole(userService.findRole(UserDAO.ROLE_NAME_CLIENT));
					User newUser = userService.createUser(user);
					if ( newUser == null  ){
						messages.add("User " + user.getName() + " didn't log on.");
						messages.add("Internal error during user creation.");
						logger.trace("User " + user.getName() + " didn't log on.");
						logger.trace("Internal error during user creation.");
						user = null;
					} else {
						messages.add("User " + user.getName() + " was successfully registered.");
						logger.trace("User " + user.getName() + " was successfully registered.");
						user = newUser;
					}
				}
			} else {
				if ( userName != null && userService.hasUser(userName) ) {
					User user = userService.getUser(userName);
					if ( user.getPassword().equalsIgnoreCase(userPass) ) {
						this.user = user;
						messages.add("User " + user.getName() + " successfully logged on.");
						logger.trace("User " + user.getName() + " successfully logged on.");
					} else {
						messages.add("Password for user " + user.getName() + " is incorrect.");
						logger.trace("Password for user " + user.getName() + " is incorrect.");
					}
				} else if ( userPass != null ) {
					messages.add("User name isn't specified for logon.");
					logger.trace("User name isn't specified for logon.");
				}
			}
		} if ("1".equals(userLogoff)) {
			if ( user != null ) {
				messages.add("User " + user.getName() + " was logged off.");
				logger.trace("User " + user.getName() + " was logged off.");
			} else {
				messages.add("No users logged in the system.");
				logger.trace("No users logged in the system.");
			}
			
			user = null;
			session.removeAttribute("recipeCart");
			session.removeAttribute("cart");
			
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
	
	public int getPagesCount() {
		return pagesCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}
	
	public String getName() {
		return name;
	}
	
	public HttpSession getSession() {
		return session;
	}
	
	public void setSession(HttpSession session) {
		this.session = session;
	}

}
