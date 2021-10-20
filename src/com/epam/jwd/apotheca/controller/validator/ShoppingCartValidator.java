package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.ShoppingCart;

public class ShoppingCartValidator implements Validator {

	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartValidator.class);
	private ShoppingCart cart;
	
	public ShoppingCartValidator() {
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		messages.clear();
		
		if ( cart.getProducts().isEmpty() ) {
			messages.add("Cart is empty.");
			logger.error("cart is empty");
			result = false;
		} else {
			//TODO 1.кол-во всех лек-в в корзине <= quantity 
			//TODO 2.ecли лек-во было в свободном доступе,а стало по рецепту 
			//TODO 3.ecли истёк срок годности рецепта на лек-во
			//TODO 4.ecли изменилась цена
			
		}
		
		return result;
		
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	@Override
	public void setValue(Object value) {
		cart = (ShoppingCart)value;
	}
	
}
