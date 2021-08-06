package com.epam.jwd.apotheca.dao.api;

import java.util.List;

import com.epam.jwd.apotheca.model.Drug;

public interface DrugDAO extends DAO<Drug> {
	
	Drug findById(Integer id);
	
	List<Drug> findByRange(Integer start, Integer end);
	
	Drug findDrug(String name, Double dose);
	
	List<Drug> findByIds(Integer... ids);
	
}
