package com.epam.jwd.apotheca.controller;

import java.util.GregorianCalendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hello implements RunCommand {

	private static final Logger logger = LoggerFactory.getLogger(Hello.class);
	private String actionTime; 
	private Map<String, String[]> params;
	
	public Hello() {
		
	}
	
	public String getActionTime() {
		return actionTime;
	}

	public String getView() {
		return "hello.jsp";
	}

	@Override
	public String run() {
		
		logger.info("hello from Hello!");
		actionTime = GregorianCalendar.getInstance().getTime().toString(); 
		return actionTime;
	}
	
	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

}
