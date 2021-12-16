package com.epam.jwd.apotheca.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.epam.jwd.apotheca.model.Drug;

public class RecipeCart {

	private List<Drug> drugs = new ArrayList<Drug>();
	private Date expieryDate;
	private Integer userId;
	private boolean invalid;

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

	public Date getExpieryDate() {
		return expieryDate;
	}

	public void setExpieryDate(Date expieryDate) {
		this.expieryDate = expieryDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	
	public String getMonth() {	 
	
		Integer month = Integer.valueOf( getField(Calendar.MONTH) ) + 1 ;
		return String.valueOf( month < 10 ? ( "0" + month ) : month );
		
	}
	
	public String getDay() {
		
		String day = getField( Calendar.DAY_OF_MONTH );
		return Integer.valueOf( day ) < 10 ? "0" + day : day;
		
	}
	
	public String getYear() {
			
		return getField( Calendar.YEAR );
		
	}
	
	
	private String getField(int field) {
		
		String fieldValue = null;
		if ( expieryDate != null ) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expieryDate);
			fieldValue = String.valueOf( calendar.get( field ) );
		}
		return fieldValue;
		
	}
	
}