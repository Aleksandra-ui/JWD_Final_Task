package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.ShoppingCart;

public interface ShoppingCartAware {
	
	ShoppingCart getCart();
	
	void setCart(ShoppingCart cart);

}
