package com.epam.jwd.apotheca.controller;

import java.util.Map;

public interface RunCommand {

	String run();
	
	String getView();
	
	void setParams(Map<String, String[]> params);
	
}
