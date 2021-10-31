package com.epam.jwd.apotheca.controller.action;

import java.util.Calendar;
import java.sql.Date;

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
		
		if ( getCart().getExpieryDate()  == null ) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(System.currentTimeMillis()));
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			calendar.add(Calendar.DAY_OF_MONTH, - 1 );
			getCart().setExpieryDate(new Date(calendar.getTime().getTime()));
		}
		
		updateCart();
		
		return null;
		
	}

}
