package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.controller.ShoppingCart;
import com.epam.jwd.apotheca.controller.validator.ShoppingCartValidator;
import com.epam.jwd.apotheca.controller.validator.Validator;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public abstract class CartAction implements RunCommand, ShoppingCartAware {

	private User user;
	private ShoppingCart cart;
	private Integer pageSize;
	private Integer currentPage;
	private Map<String, String[]> params;
	private Map<Drug, Integer> products;
	private Validator validator;
	protected Map<Drug, Map<String, String>> invalidDrugs;
	
	public CartAction() {
		validator = new ShoppingCartValidator();
		invalidDrugs = new HashMap<Drug, Map<String, String>>();
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
	
	@Override
	public boolean isSecure() {
		return true;
	}

	@Override
	public String getView() {
		return "cart.jsp";
	}

	public Integer getPageSize() {
		return pageSize;
	}

	
	public Integer getCurrentPage() {
		return currentPage;
	}

	
	public ShoppingCart getCart() {
		return cart;
	}
	
	
	public void setParams(Map<String, String[]> params) {
		this.params = params;
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

	public Validator getValidator() {
		return validator;
	}

	public void updateProducts() {
		Map<Drug, Integer> drugsToDisplay = getCart().getProducts(getPageSize() * (getCurrentPage() - 1), getPageSize());
		//запустить валидатор shoppingcart
		//ecли валидатор имеет замечания,сетить в карт invalid(true),иначе false
		getCart().setInvalid(false);
		getValidator().setValue(getCart());
		if ( ! getValidator().validate() ) {
			getCart().setInvalid(true);
		}
		
		if ( getCart().isInvalid() ) {
			
			invalidDrugs.clear();
			//заполнять список невалидными драгами по примеру shoppingCartValidator
			//итер по drugsToDisplay
			List<Integer> ids = new ArrayList<Integer>(drugsToDisplay.size());
			drugsToDisplay.keySet().stream().forEach(d -> { ids.add(d.getId()); });
			
			List<Drug> updated = DrugManagerService.getInstance().getDrugs(ids.toArray(new Integer[ids.size()]));
			Drug actualDrug;
			for ( Drug drug : drugsToDisplay.keySet() ) {
				//TODO 1.кол-во всех лек-в в корзине <= quantity 
				actualDrug = null;
				for ( Drug updatedDrug : updated ) {
					if ( updatedDrug.getId() == drug.getId() ) {
						actualDrug = updatedDrug;
						break;
					}
				}
				
				Map<String, String> errorColumns = new HashMap<String, String>();
				
				if ( actualDrug == null ) {
					errorColumns.put("absent", "1");
				} else {
					Integer amountToBuy = drugsToDisplay.get(drug);
					if ( amountToBuy > actualDrug.getQuantity() ) {
						errorColumns.put("amount", String.valueOf(actualDrug.getQuantity()));
					}
					//TODO 2.ecли лек-во было в свободном доступе,а стало по рецепту 
					if ( ! drug.isPrescription() && actualDrug.isPrescription() ) {
						errorColumns.put("prescription", "prescripted");
					}
					//TODO 3.ecли истёк срок годности рецепта на лек-во
					//TODO 4.ecли изменилась цена
					if ( actualDrug.getPrice() > drug.getPrice() ) {
						errorColumns.put("price", String.valueOf(actualDrug.getPrice()));
					}
				}
				
				if ( ! errorColumns.isEmpty() ) {
					invalidDrugs.put(drug, errorColumns);
				}
				
			}
		
		}
		
		setProducts( drugsToDisplay );
	}

	public Map<Drug, Map<String, String>> getInvalidDrugs() {
		return invalidDrugs;
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
