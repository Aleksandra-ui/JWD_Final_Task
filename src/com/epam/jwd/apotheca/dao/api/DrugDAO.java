package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.Drug;

public interface DrugDAO extends DAO<Drug> {
	
	Drug findById(Integer id);
	
	List<Drug> findByRange(Integer start, Integer count, String columnName);
	
	Drug findDrug(String name, Double dose);
	
	List<Drug> findByIds(Integer... ids);
	
	List<Drug> findPrescripted();
	
	Integer getPrescriptedCount();
	
	boolean changeQuantity(Drug drug, Integer amount);
	
}
