package com.epam.jwd.apotheca.controller.action;

public class DisplayCart extends CartAction {
	
	private static DisplayCart instance = new DisplayCart();
	
	private DisplayCart() {
	}
	
	public static DisplayCart getInstance() {
		return instance;
	}

	@Override
	public void run() {

		super.run();
		updateProducts();
		
	}
	
}
