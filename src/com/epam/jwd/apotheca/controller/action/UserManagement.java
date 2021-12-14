package com.epam.jwd.apotheca.controller.action;

import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.User;

public class UserManagement implements RunCommand {

	private static final String name = "UserManagement";
	private static UserManagement instance = new UserManagement();
	private Map<String, String[]> params;
	private User user;
	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private List<User> users;
	
	public UserManagement() {
	}
	
	public static UserManagement getInstance() {
		return instance;
	}

	@Override
	public void run() {
		
		UserManagerService userService = UserManagerService.getInstance();
		
		totalCount = userService.getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		
		users = userService.findUsersByRange(pageSize * (currentPage - 1), pageSize);
		
	}

	@Override
	public String getView() {
		return "secure/userManagement.jsp";
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
	
	public List<User> getUsers() {
		return users;
	}

}
