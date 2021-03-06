package com.epam.jwd.apotheca.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.epam.jwd.apotheca.dao.api.DrugDAO;
import com.epam.jwd.apotheca.dao.impl.DrugDAOImpl;
import com.epam.jwd.apotheca.model.Drug;

public class DrugManagerService {
	
	private static DrugManagerService instance = new DrugManagerService();
	private DrugDAO drugDAO;
	
	private DrugManagerService() {
		drugDAO = DrugDAOImpl.getInstance();
	}
	
	public static DrugManagerService getInstance() {
		return instance;
	}

	public List<Drug> getDrugs() {
		return drugDAO.findAll();
	}
	
	public List<Drug> getDrugs(int start, int count, String sortColumn) {
		return drugDAO.findByRange(start, count, sortColumn);
	}

	public List<Drug> getPrescriptedDrugs() {
		return ((DrugDAOImpl) drugDAO).findPrescripted();
	}

	public List<Drug> getPrescriptedDrugs(int start, int end) {
		return ((DrugDAOImpl) drugDAO).findPrescriptedByRange(start, end);
	}

	public boolean addDrug(Drug newDrug) {

		Drug existingDrug = drugDAO.findDrug(newDrug.getName(), newDrug.getDose());
		if (existingDrug != null) {
			existingDrug.setQuantity(existingDrug.getQuantity() + newDrug.getQuantity());
			existingDrug.setPrescription(newDrug.isPrescription());
			existingDrug.setPrice(newDrug.getPrice());
			drugDAO.update(existingDrug);
			return true;
		} else {
			return drugDAO.save(newDrug) != null;
		}

	}

	public Drug getDrug(Integer id) {

		return drugDAO.findById(id);

	}

	public List<Drug> getDrugs(Integer... ids) {

		return drugDAO.findByIds(ids);

	}

	public List<Drug> getDrugs(String... ids) {

		List<Integer> intIds = new ArrayList<Integer>();
		Arrays.asList(ids).stream().forEach(id -> intIds.add(Integer.valueOf(id)));
		return drugDAO.findByIds(intIds.toArray(new Integer[intIds.size()]));

	}
	
	public Integer getTotalCount() {
		return drugDAO.getTotalCount();
	}

	public Integer getPrescriptedCount() {
		return drugDAO.getPrescriptedCount();
	}
	
}
