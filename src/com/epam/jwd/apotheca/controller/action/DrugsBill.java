package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.Order;
import com.epam.jwd.apotheca.model.User;

public class DrugsBill implements RunCommand {

	private static DrugsBill instance = new DrugsBill();
	private static final Logger logger = LoggerFactory.getLogger(DrugsBill.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	private List<Drug> drugs; 
	private Map<Drug, Integer> amountsById;
	private AtomicInteger total;
	private Order order;
	
	private DrugsBill() {
		drugs = new ArrayList<Drug>(); 
		amountsById = new HashMap<Drug, Integer>();
		total = new AtomicInteger(0);
	}

	public static DrugsBill getInstance() {
		return instance;
	}
	
	@Override
	public String run() {
				
		total.set(0);
		
		if (params.get("drugIds") != null){
			String[] drugIdsStr = params.get("drugIds")[0].split(",");
			drugs.addAll(DrugManagerService.getInstance().getDrugs(drugIdsStr)); 
			
			String[] amountsStr = params.get("amounts")[0].split(",");
			for ( int i = 0; i < drugIdsStr.length; i ++ ) {
				amountsById.put(drugs.get(i), Integer.valueOf(amountsStr[i]));
			}
				
			order = OrderManagerService.getInstance().buy(user.getId(), amountsById);
			
			if ( order != null ) {
				for ( Map.Entry<Drug, Integer> e : order.getDrugs().entrySet() ) {
					total.addAndGet(e.getKey().getPrice() * e.getValue());
				}
			}
		}
		
		return actionTime;
		
	}

	@Override
	public String getView() {
		return "drugsBill1.jsp";
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
	
	public List<Drug> getDrugs() {
		return drugs;
	}

	public Map<Drug, Integer> getAmountsById() {
		return amountsById;
	}

	public AtomicInteger getTotal() {
		return total;
	}

	public Order getOrder() {
		return order;
	}
	
}
