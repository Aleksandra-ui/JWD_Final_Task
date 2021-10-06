package com.epam.jwd.apotheca.controller.action;

import java.util.Map;

import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.model.Drug;

public abstract class CartAction implements RunCommand, ShoppingCartAware {

	private ShoppingCart cart;
	private Integer pageSize;
	private Integer currentPage;
	private Map<String, String[]> params;
	private Map<Drug, Integer> products;
	
	public CartAction() {
		
	}
	
	public Integer getTotalCount() {
		
		return cart.getProducts().size();
	}
	
	
	@Override
	public String run() {
		
		pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
		currentPage = params.get("currentPage") == null ? 1
				: Integer.valueOf(params.get("currentPage")[0]);
		
		return null;
		
	}

	public Integer getPageSize() {
		// TODO Auto-generated method stub
		return pageSize;
	}

	
	public Integer getCurrentPage() {
		// TODO Auto-generated method stub
		return currentPage;
	}

	
	public ShoppingCart getCart() {
		return cart;
	}
	
	
	public void setParams(Map<String, String[]> params) {
		this.params=params;
	}

	public Map<Drug, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<Drug, Integer> products) {
		this.products = products;
	}

	protected Map<String, String[]> getParams() {
		
		return params;
	}
	
	@Override
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

}
