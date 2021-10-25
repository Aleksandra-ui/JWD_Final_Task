package com.epam.jwd.apotheca.controller.action;

public class DisplayRecipeCart extends RecipeCartAction implements RecipeCartAware {

	public DisplayRecipeCart() {
		
	}
	
	@Override
	public String run() {
		
		super.run();
		
		updateCart();
		return null;
	}

}
