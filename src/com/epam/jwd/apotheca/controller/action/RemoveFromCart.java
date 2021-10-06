package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class RemoveFromCart extends CartAction {

	public RemoveFromCart() {
	}

	@Override
	public String run() {

		super.run();
		
		String id = getParams().get("drugId") == null ? null : getParams().get("drugId")[0];
		
		if ( id != null ) {
			getCart().removeDrug(DrugManagerService.getInstance().getDrug(Integer.valueOf(id)));
		}
		
		setProducts( getCart().getProducts(getPageSize() * (getCurrentPage() - 1), getPageSize()) );
		
		return null;
	}

	@Override
	public String getView() {
		// TODO Auto-generated method stub
		return "cart.jsp";
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

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return true;
	}

}
