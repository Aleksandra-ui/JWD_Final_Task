package com.epam.jwd.apotheca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.model.Drug;

public class ShoppingCart {

	private Map<Drug, Integer> products = new HashMap<Drug, Integer>();
	
	public void addDrug(Drug drug, Integer quantity) {
		
		products.put(drug, quantity);
		
	}
	
	public void removeDrug(Drug drug) {
		
		products.remove(drug);
		
	}
	
	public void updateDrug(Drug drug, Integer quantity) {
		
		products.put(drug, quantity);
		
	}
	
	public Map<Drug, Integer> getProducts() {
		return products;
	}
	
}
