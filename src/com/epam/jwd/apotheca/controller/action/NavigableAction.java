package com.epam.jwd.apotheca.controller.action;

import java.util.Map;

import com.epam.jwd.apotheca.model.User;

public abstract class NavigableAction implements RunCommand {

	private int totalCount;
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private Map<String, String[]> params;
	
	public NavigableAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String run() {
		totalCount = 0;
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
		return null;
	}

	@Override
	public String getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public Map<String, String[]> getParams() {
		return params;
	}
	
}
