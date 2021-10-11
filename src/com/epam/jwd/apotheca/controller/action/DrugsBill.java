package com.epam.jwd.apotheca.controller.action;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class DrugsBill implements RunCommand, ShoppingCartAware {

	private static DrugsBill instance = new DrugsBill();
	private static final Logger logger = LoggerFactory.getLogger(DrugsBill.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	private AtomicInteger total;
	private Order order;
	private ShoppingCart cart;
	
	private DrugsBill() {
		total = new AtomicInteger(0);
	}

	public static DrugsBill getInstance() {
		return instance;
	}
	
	@Override
	public String run() {
		if ( getCart().getProducts().size() > 0 ){
			
			order = OrderManagerService.getInstance().buy(user.getId(), getCart().getProducts());
			
			if ( order != null ) {
				getCart().getProducts().clear();
				total.set(0);
				for ( Map.Entry<Drug, Integer> e : order.getDrugs().entrySet() ) {
					total.addAndGet(e.getKey().getPrice() * e.getValue());
				}
			}
			
		}
		
		return actionTime;
		
	}

	@Override
	public String getView() {
		return "secure/drugsBill1.jsp";
	}

	@Override
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return true;
	}

	public String getActionTime() {
		return actionTime;
	}

	public AtomicInteger getTotal() {
		return total;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public ShoppingCart getCart() {
		return cart;
	}

	@Override
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}
	
}
