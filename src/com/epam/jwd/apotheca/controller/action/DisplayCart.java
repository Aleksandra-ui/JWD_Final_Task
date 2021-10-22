package com.epam.jwd.apotheca.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.jwd.apotheca.controller.DrugManagerService;
import com.epam.jwd.apotheca.model.Drug;
import com.epam.jwd.apotheca.model.User;

public class DisplayCart extends CartAction {

	private User user;
	private Map<Drug, Drug> invalidDrugs;
	
	public DisplayCart() {
		
		invalidDrugs = new HashMap<Drug, Drug>();
		
	}

	@Override
	public String run() {

		super.run();
		Map<Drug, Integer> drugsToDisplay = getCart().getProducts(getPageSize() * (getCurrentPage() - 1), getPageSize());
		//запустить валидатор shoppingcart
		//ecли валидатор имеет замечания,сетить в сарт invalid(true),иначе false
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
				
				Integer amountToBuy = drugsToDisplay.get(drug);
				if ( amountToBuy > actualDrug.getQuantity() ) {
					invalidDrugs.putIfAbsent(drug, actualDrug);
				}
				//TODO 2.ecли лек-во было в свободном доступе,а стало по рецепту 
				if ( ! drug.isPrescription() && actualDrug.isPrescription() ) {
					invalidDrugs.putIfAbsent(drug, actualDrug);
				}
				//TODO 3.ecли истёк срок годности рецепта на лек-во
				//TODO 4.ecли изменилась цена
				if ( actualDrug.getPrice() > drug.getPrice() ) {
					invalidDrugs.putIfAbsent(drug, actualDrug);
				}
				
			}
		
		}
		
		setProducts( drugsToDisplay );
		
		return null;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	public Map<Drug, Drug> getInvalidDrugs() {
		return invalidDrugs;
	}
	
}
