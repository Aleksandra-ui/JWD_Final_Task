package com.epam.jwd.apotheca.controller.action;

public class DisplayCart extends CartAction {
	
	public DisplayCart() {
	}

	@Override
	public String run() {

		super.run();
		updateProducts();
		
		return null;
	}
	
}
