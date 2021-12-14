package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;

public class RemoveFromRecipeCart extends RecipeCartAction {

	private static RemoveFromRecipeCart instance = new RemoveFromRecipeCart();
	
	private RemoveFromRecipeCart() {
	}
	
	public static RemoveFromRecipeCart getInstance() {
		return instance;
	}

	@Override
	public void run() {
		
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
		
	}
	
}