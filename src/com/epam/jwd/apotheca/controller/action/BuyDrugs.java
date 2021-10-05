package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.OrderManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class BuyDrugs implements RunCommand {

	private static BuyDrugs instance = new BuyDrugs();
	private static final Logger logger = LoggerFactory.getLogger(BuyDrugs.class);
	private String actionTime;
	private Map<String, String[]> params;
	private User user;
	List<Drug> drugs; 
	Map<Drug, Integer> amountsById;
	
	private BuyDrugs() {
		drugs = new ArrayList<Drug>(); 
		amountsById = new HashMap<Drug, Integer>();
	}
	
	public static BuyDrugs getInstance() {
		return instance;
	}

	@Override
	public String run() {
		
		String[] drugIdsStr = params.get("drugIds")[0].split(",");
		
		drugs.addAll(DrugManagerService.getInstance().getDrugs(drugIdsStr)); 
		
		String[] amountsStr = params.get("amounts")[0].split(",");
		for ( int i = 0; i < drugIdsStr.length; i ++ ) {
			amountsById.put(drugs.get(i), Integer.valueOf(amountsStr[i]));
		}
					
		OrderManagerService.getInstance().buy(user.getId(), amountsById);
		
		return actionTime;
		
	}

	@Override
	public String getView() {
		return "buyDrugs1.jsp";
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
		return false;
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
	
}
