package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;

public class RemoveFromRecipeCart extends RecipeCartAction implements RecipeCartAware {

	public RemoveFromRecipeCart() {
	}

	@Override
	public String run() {
		
		super.run();
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		
		if ( id != null ) {
			getCart().removeDrug(DrugManagerService.getInstance().getDrug(Integer.valueOf(id)));
		}
		
		if ( getCart().getDrugs().isEmpty() ) {
			getCart().setExpieryDate(null);
			getCart().setUserId(null);
		}
		
		updateCart();
		return null;
		
	}
	
}