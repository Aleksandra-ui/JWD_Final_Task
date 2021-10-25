package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.controller.validator.UserIdValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.User;

public class SetClientName extends RecipeCartAction {

	private static SetClientName instance = new SetClientName();
	private Integer clientId;
	
	private SetClientName() {
		super();
		getValidators().put("client", new UserIdValidator("clientId"));
	}
	
	public static SetClientName getInstance() {
		return instance;
	}

	@Override
	public String run() {
		
		super.run();
		
		Validator validator = getValidators().get("client");
		validator.setValue(getParams());
		
		if ( validator.validate() ) {
			
			clientId = Integer.valueOf(getParams().get("clientId")[0]);
			User client = UserManagerService.getInstance().getUser(clientId);
			if ( client != null ) {
				getCart().setUserId(client.getId());
			}
			
		}
		
		updateCart();
		
		return null;
	}

}
