package com.epam.jwd.apotheca.controller;

import java.util.Map;
import java.util.TreeMap;

import com.epam.jwd.apotheca.model.Drug;

public class ShoppingCart {

	private Map<Drug, Integer> products = new TreeMap<Drug, Integer>();

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
	
	public Map<Drug, Integer> getProducts(int start, int count) {
		
		Drug[] drugs = products.keySet().toArray(new Drug[products.size()]);
		
		Map <Drug, Integer> chosenProducts = new TreeMap<Drug, Integer>();
		if ( start >= 0 && start < drugs.length ) {
			int realCount = Math.min( drugs.length - start, count );
			Drug[] chosenDrugs = new Drug[realCount];
			System.arraycopy(drugs, start, chosenDrugs, 0, realCount);
			for ( Drug drug : chosenDrugs ) {
				chosenProducts.put(drug, products.get(drug));
			}
		}
		
		return chosenProducts;
		
	}

}