package com.epam.jwd.apotheca.model;

import java.sql.Date;
import java.util.List;

public class Order implements Entity {

	private Integer id;
	private Integer drugId;
	private Integer amount;
	private List<Integer[]> drugs;
	private Integer userId;
	private Date date;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDrugId() {
		return drugId;
	}
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public List<Integer[]> getDrugs() {
		return drugs;
	}
	public void setDrugs(List<Integer[]> drugs) {
		this.drugs = drugs;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
