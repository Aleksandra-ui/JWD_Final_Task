package com.epam.jwd.apotheca.controller;

import java.util.Map;

import com.epam.jwd.apotheca.model.User;

public interface RunCommand {

	String run();
	
	String getView();
	
	void setParams(Map<String, String[]> params);
	
	void setUser(User user);
	
}