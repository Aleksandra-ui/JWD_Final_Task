package com.epam.jwd.apotheca.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.epam.jwd.apotheca.model.Drug;

public class RecipeCart {

	private List<Drug> drugs = new ArrayList<Drug>();
//	private Date expieryDate;
//	private Integer userId;

	public void addDrug(Drug drug) {

		drugs.add(drug);
		
	}

	public void removeDrug(Drug drug) {

		drugs.remove(drug);

	}
	
	public void clear() {
		drugs.clear();
	}

	public List<Drug> getDrugs() {
		return new ArrayList<Drug>( drugs );
	}
	
	public List<Drug> getDrugs(int start, int count) {
		
		if ( start >= 0 && start < drugs.size() ) {
			int realCount = Math.min( drugs.size() - start, count );
			return new ArrayList<Drug>( drugs ).subList(start, start + realCount);
		} else {
			return Collections.emptyList();
		}
		
	}

//	public Date getExpieryDate() {
//		return expieryDate;
//	}
//
//	public void setExpieryDate(Date expieryDate) {
//		this.expieryDate = expieryDate;
//	}
//
//	public Integer getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Integer userId) {
//		this.userId = userId;
//	}

}