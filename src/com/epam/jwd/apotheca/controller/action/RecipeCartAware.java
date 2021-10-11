package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.ShoppingCart;

public interface RecipeCartAware {
	
	RecipeCart getCart();
	
	void setCart(RecipeCart cart);

}
