package com.epam.jwd.apotheca.controller.validator;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.RecipeCart;
import com.epam.jwd.apotheca.controller.UserManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

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
			//TODO 2.ecли дата истечения  рецепта вышла за пределы допустимого интервала
			Date currentDate = new Date(System.currentTimeMillis());
			if ( currentDate.after(cart.getExpieryDate()) ) {
				messages.add("Cannot create a recipe. It's out of date." );
				logger.error("cannot create a recipe. It's out of date" );
				result = false;
			//TODO 3.существует ли польз-тель в системе
			} else {
				User client = UserManagerService.getInstance().getUser(cart.getUserId());
				if ( client == null ) {
					messages.add("Cannot create a recipe. Specified client is not present in the system.");
					logger.error("cannot create a recipe.  Specified client is not present in the system");
					result = false;
				} else {
					for ( Drug d : cart.getDrugs() ) {
						Drug actualDrug = DrugManagerService.getInstance().getDrug(d.getId());
						//TODO 1.ecли лек-во было по рецепту,а стало в свободном доступе
						if ( ! actualDrug.isPrescription() ) {
							messages.add("Cannot create a recipe for drug '" + d.getName() + "'. It now does not require prescription." );
							logger.error("cannot create a recipe for drug '" + d.getName() + "'. It now does not require prescription");
							result = false;
							break;
						}
					}
				}
			}
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
