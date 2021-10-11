package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class AddToCart extends CartAction {

	public AddToCart() {
	}

	@Override
	public String run() {

		super.run();
		
		String amount = getParams().get("amount") == null ? null : getParams().get("amount")[0];
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		if ( id != null && amount != null ) {
			Drug drug = DrugManagerService.getInstance().getDrug(Integer.valueOf(id));
			if ( drug != null ) {
				getCart().addDrug(drug, Integer.valueOf(amount));
			}
		}
		
		setProducts( getCart().getProducts(getPageSize() * (getCurrentPage() - 1), getPageSize()) );
		
		return null;
	}

	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}


}
