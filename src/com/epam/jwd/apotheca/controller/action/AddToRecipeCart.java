package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.model.Drug;

public class AddToRecipeCart extends RecipeCartAction implements RecipeCartAware {

	public AddToRecipeCart() {
		
	}

	@Override
	public String run() {

		super.run();
		
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		if ( id != null ) {
			Drug drug = DrugManagerService.getInstance().getDrug(Integer.valueOf(id));
			if ( drug != null ) {
				getCart().addDrug(drug);
			}
		}
		
		
		
		updateCart();
		
		return null;
	}

}
