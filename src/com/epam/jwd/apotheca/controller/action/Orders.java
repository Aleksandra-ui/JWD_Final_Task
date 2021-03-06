package com.epam.jwd.apotheca.controller.action;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.model.User;

public class Orders implements RunCommand {

	private static final String name = "Orders";
	private static Orders instance = new Orders();
	private static final Logger logger = LoggerFactory.getLogger(Orders.class);
	private Map<String, String[]> params;
	private User user;
	private List<Map<String, String>> drugsInfo;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private int totalCount;
	
	private Orders() {
	}
	
	public static Orders getInstance() {
		return instance;
	}

	@Override
	public void run() {

		totalCount = OrderManagerService.getInstance().getDrugsCountByUser(user.getId());
		
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		
		int startIndex =  pageSize * (currentPage - 1);
		int count =  currentPage < pagesCount ? pageSize : totalCount % pageSize ;
		drugsInfo = OrderManagerService.getInstance().findDrugInfoByRange(user, startIndex, count);
		
		logger.trace("displaying " + count + " drugs ordered by user " + user + " starting from " + startIndex + " records");
		
	}

	@Override
	public String getView() {
		return "secure/orders.jsp";
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
	
	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getPagesCount() {
		return pagesCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public List<Map<String, String>> getDrugsInfo() {
		return drugsInfo;
	}
	
	public String getName() {
		return name;
	}
	
}
