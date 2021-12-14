package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.model.Drug;

public class AddToCart extends CartAction {
	
	private static AddToCart instance = new AddToCart();

	private AddToCart() {
	}
	
	public static AddToCart getInstance() {
		return instance;
	}

	@Override
	public void run() {

		super.run();
		
		String amount = getParams().get("amount") == null ? null : getParams().get("amount")[0];
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		if ( id != null && amount != null ) {
			Drug drug = DrugManagerService.getInstance().getDrug(Integer.valueOf(id));
			if ( drug != null ) {
				getCart().addDrug(drug, Integer.valueOf(amount));
			}
		}

		updateProducts();
		
	}

}
