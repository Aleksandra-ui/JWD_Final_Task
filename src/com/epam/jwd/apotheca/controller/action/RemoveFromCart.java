package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;

public class RemoveFromCart extends CartAction {
	
	public RemoveFromCart() {
		
		
	}

	@Override
	public String run() {

		super.run();
		
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		
		if ( id != null ) {
			getCart().removeDrug(DrugManagerService.getInstance().getDrug(Integer.valueOf(id)));
		}
		
		updateProducts();
		
		return null;
	}

}