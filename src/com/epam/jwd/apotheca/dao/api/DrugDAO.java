package com.epam.jwd.apotheca.dao.api;

import com.epam.jwd.apotheca.model.Drug;

public interface DrugDAO extends DAO<Drug> {
	
	Drug findById(Integer id);

}
