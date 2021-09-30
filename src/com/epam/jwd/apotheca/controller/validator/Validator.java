package com.epam.jwd.apotheca.controller.validator;

import java.util.List;

public interface Validator {

	List<String> getMessages();
	
	boolean validate();
	
}
