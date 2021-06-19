package com.epam.jwd.apotheca.model;

public class Drug  implements Entity {

	private Integer id;
	private String name;
	private Integer quantity;
	private Integer price;
	private Integer dose;
	private boolean prescription;
	
	@Override
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getDose() {
		return dose;
	}
	public void setDose(Integer dose) {
		this.dose = dose;
	}
	public boolean isPrescription() {
		return prescription;
	}
	public void setPrescription(boolean prescription) {
		this.prescription = prescription;
	}
	
	

	
	
}
