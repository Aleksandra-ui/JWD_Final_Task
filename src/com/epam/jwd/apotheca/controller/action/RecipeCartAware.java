package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.RecipeCart;

public interface RecipeCartAware {
	
	RecipeCart getCart();
	
	void setCart(RecipeCart cart);

}
