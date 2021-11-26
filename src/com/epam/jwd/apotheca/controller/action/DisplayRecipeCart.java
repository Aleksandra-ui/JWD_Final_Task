package com.epam.jwd.apotheca.controller.action;

public class DisplayRecipeCart extends RecipeCartAction implements RecipeCartAware {

	private static DisplayRecipeCart instance = new DisplayRecipeCart();
	
	private DisplayRecipeCart() {
	}
	
	public static DisplayRecipeCart getInstance() {
		return instance;
	}
	
	@Override
	public String run() {
		
		super.run();
		
		updateCart();
		return null;
	}

}
