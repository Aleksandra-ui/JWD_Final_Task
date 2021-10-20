package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.RecipeCart;

public class RecipeCartValidator implements Validator {

	private List<String> messages;
	private static final Logger logger = LoggerFactory.getLogger(RecipeCartValidator.class);
	private RecipeCart cart;
	
	public RecipeCartValidator() {
		messages = new ArrayList<String>();
	}
	
	public boolean validate() {
		
		boolean result = true;
		messages.clear();
		
		if ( cart.getDrugs().isEmpty() ) {
			messages.add("Cart is empty.");
			logger.error("cart is empty");
			result = false;
		} else {
			
			//TODO 1.ecли лек-во было по рецепту,а стало в свободном доступе
			//TODO 2.ecли дата истечения  рецепта вышла за пределы допустимого интервала
			//TODO 3.существует ли польз-тель в системе
			
		}
		
		return result;
		
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	@Override
	public void setValue(Object value) {
		cart = (RecipeCart)value;
	}
	
}
