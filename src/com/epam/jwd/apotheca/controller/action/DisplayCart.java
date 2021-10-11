package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.model.User;

public class DisplayCart extends CartAction {

	public DisplayCart() {
	}

	@Override
	public String run() {

		super.run();
		
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
