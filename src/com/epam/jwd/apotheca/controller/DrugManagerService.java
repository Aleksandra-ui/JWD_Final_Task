package com.epam.jwd.apotheca.controller;

import java.util.List;


import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.model.Drug;

public class DrugManagerService {

	private DrugDAO drugDAO;

	public DrugManagerService() {
		drugDAO = new DrugDAOImpl();
	}

	public DrugDAO getDrugDAO() {
		return drugDAO;
	}
	
	public List<Drug> getDrugs(){
		return drugDAO.findAll();
	}
	
	public List<Drug> getDrugs(int start, int end){
		return drugDAO.findById(start, end);
	}
	
	public List<Drug> getPrescriptedDrugs(){
		return ((DrugDAOImpl)drugDAO).findPrescripted();
	}
	
	public List<Drug> getPrescriptedDrugs(int start, int end){
		return ((DrugDAOImpl)drugDAO).findPrescriptedById(start, end);
	}
	
	public boolean addDrug(Drug newDrug) {
		
		Drug existingDrug = drugDAO.findDrug(newDrug.getName(), newDrug.getDose()); 
		if ( existingDrug != null ) {
			existingDrug.setQuantity(existingDrug.getQuantity() + newDrug.getQuantity());
			existingDrug.setPrescription(newDrug.isPrescription());
			existingDrug.setPrice(newDrug.getPrice());
			drugDAO.update(existingDrug);
			return true;
		}
		else {
			return drugDAO.save(newDrug) != null;
		}
		
	}
	
	public Drug getDrug(Integer id) {
		
		return drugDAO.findById(id);
		
	}
	
}
