package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
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
	private int pageSize;
	private int currentPage;
	private int pagesCount;
	private int totalCount;
	private Map<Drug, Integer> drugs;
	private List<String> errorMessages;
	
	private DrugsBill() {
		total = new AtomicInteger(0);
		drugs = new TreeMap<Drug, Integer>();
		errorMessages = new ArrayList<String>();
	}

	public static DrugsBill getInstance() {
		return instance;
	}
	
	@Override
	public String run() {
		errorMessages.clear();
		if ( getCart().getProducts().size() > 0 ){
			order = OrderManagerService.getInstance().buy(user.getId(), getCart().getProducts());
			if ( order != null ) {
				getCart().getProducts().clear();
				total.set(0);
				for ( Map.Entry<Drug, Integer> e : order.getDrugs().entrySet() ) {
					total.addAndGet(e.getKey().getPrice() * e.getValue());
				}
				totalCount = order.getDrugs().size();
			} else {
				//создать валидатор количества доступных лек-в,ззапустить и рез-тат добавить в errorMessages
				errorMessages.add("Cannot create an order.");
			}
		} 
		if ( order != null ) {
			pageSize = params.get("pageSize") == null ? 5 : Integer.valueOf(params.get("pageSize")[0]);
			currentPage = params.get("currentPage") == null ? 1
					: Integer.valueOf(params.get("currentPage")[0]);
			pagesCount = totalCount / pageSize + ((totalCount % pageSize) == 0 ? 0 : 1);
//			List<Drug> drugs = DrugManagerService.getInstance().getDrugs(pageSize * (currentPage - 1), pageSize);
			//drugs.forEach(d -> this.drugs.put(d, order.getDrugs().get(d)));
			TreeMap<Drug, Integer> map = new TreeMap<Drug, Integer>(order.getDrugs());
			int start = pageSize * (currentPage - 1);
			int index = 0;
			System.out.println("start: " + start + ",index: " + index);
			drugs.clear();
			for ( Drug d : map.keySet() ) {
				System.out.println("index: " + index);
				if (index >= start ) {
					this.drugs.put(d, map.get(d));
					System.out.println(d.getId());
				} 
				index++;
				if(index ==  map.size()||this.drugs.size()==pageSize ) {
					System.out.println("break at index: " + index);
					break;
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
	
	public int getPagesCount() {
		return pagesCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

	public Map<Drug, Integer> getDrugs() {
		return drugs;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
}
