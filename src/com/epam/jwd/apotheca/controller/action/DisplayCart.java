package com.epam.jwd.apotheca.controller.action;

import com.epam.jwd.apotheca.model.User;

public class DisplayCart extends CartAction {

	User user;
	
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
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

}
