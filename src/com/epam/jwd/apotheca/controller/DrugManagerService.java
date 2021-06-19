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
	
}
