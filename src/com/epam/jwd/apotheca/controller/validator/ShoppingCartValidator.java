package com.epam.jwd.apotheca.controller.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.model.Drug;

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
			List<Integer> ids = new ArrayList<Integer>(cart.getProducts().size());
			cart.getProducts().keySet().stream().forEach(d -> { ids.add(d.getId()); });
			List<Drug> updated = DrugManagerService.getInstance().getDrugs(ids.toArray(new Integer[ids.size()]));
			Drug actualDrug;
			for ( Drug drug : cart.getProducts().keySet() ) {
				actualDrug = null;
				for ( Drug updatedDrug : updated ) {
					if ( updatedDrug.getId() == drug.getId() ) {
						actualDrug = updatedDrug;
						break;
					}
				}
				if (actualDrug == null) {
					messages.add("There is no such drug in DB.");
					logger.error("there is no such drug in DB");
					result = false;
				} else {
					
					Integer amountToBuy = cart.getProducts().get(drug);
					if ( amountToBuy < 1 || amountToBuy > 100 ) {
						messages.add("Amount " + amountToBuy + " exceeds allowed range (between 1 and 100).");
						logger.error("amount exceeds allowed range (between 1 and 100)");
						result = false;
					}
					
					if ( amountToBuy > actualDrug.getQuantity() ) {
						messages.add("Cannot create an order.");
						String message = String.format("Cannot sell drug '%s'. You asked for %d grand, when there are %d grand left.", drug.getName(), amountToBuy, actualDrug.getQuantity());
						messages.add(message);
						logger.error(message.toLowerCase());
						result = false;
					}
					 
					if ( ! drug.isPrescription() && actualDrug.isPrescription() ) {
						
						messages.add("Cannot create an order.");
						messages.add("Cannot sell drug '" + drug.getName() + "'. It now requires prescription.");
						logger.error("cannot sell drug '" + drug.getName() + "'. it now requires prescription");
						result = false;
						
					}
					
					if ( actualDrug.getPrice() > drug.getPrice() ) {
						messages.add("Cannot create an order.");
						messages.add("Cannot sell drug '" + drug.getName() + "'. Price for it has changed from " + drug.getPrice() + " to " + actualDrug.getPrice() + ".");
						logger.error("cannot sell drug '" + drug.getName() + "'. price for it has changed from " + drug.getPrice() + " to " + actualDrug.getPrice() + ".");
						result = false;
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
		cart = (ShoppingCart)value;
	}
	
}
