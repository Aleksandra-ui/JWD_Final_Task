package com.epam.jwd.apotheca.controller.action;

import java.util.GregorianCalendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.model.User;

public class Bye implements RunCommand {
	
	private static final Logger logger = LoggerFactory.getLogger(Bye.class);
	private String actionTime; 
	private Map<String, String[]> params;
	private User user;

	public Bye() {
	}
	
	public String getActionTime() {
		return actionTime;
	}
	
	public String getView() {
		return "bye.jsp";
	}

	@Override
	public String run() {
		
		logger.info("hello from Bye!");
		actionTime = GregorianCalendar.getInstance().getTime().toString(); 
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
	public boolean isSecure() {
		
		return false;
	}

	@Override
	public User getUser() {
		
		return user;
	}


}
