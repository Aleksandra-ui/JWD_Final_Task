package com.epam.jwd.apotheca.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.model.Drug;

public class Drugs implements RunCommand {

	private static final Logger logger = LoggerFactory.getLogger(Drugs.class);
	private String actionTime;
	private List<Drug> drugs;
	private int totalCount;
	private Map<String, String[]> params;
	private int pageSize;
	private int currentPage;

	public Drugs() {
		drugs = new ArrayList<Drug>();
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

	public String getActionTime() {
		return actionTime;
	}

	public String getView() {
		return "drugs1.jsp";
	}

	public List<Drug> getDrugs() {
		return drugs;
	}

	@Override
	public String run() {

		logger.info("hello from Drugs!");
		DrugManagerService service = DrugManagerService.getInstance();
		totalCount = service.getTotalCount();
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		drugs = service.getDrugs(pageSize * (currentPage - 1), pageSize);
		actionTime = GregorianCalendar.getInstance().getTime().toString();
		return actionTime;
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

}
