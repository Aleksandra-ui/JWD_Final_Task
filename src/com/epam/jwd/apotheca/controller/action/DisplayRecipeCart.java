package com.epam.jwd.apotheca.controller.action;

public class DisplayRecipeCart extends RecipeCartAction {

	private static DisplayRecipeCart instance = new DisplayRecipeCart();
	
	private DisplayRecipeCart() {
	}
	
	public static DisplayRecipeCart getInstance() {
		return instance;
	}
	
	@Override
	public void run() {
		
		super.run();
		
		updateCart();
		
	}

}
