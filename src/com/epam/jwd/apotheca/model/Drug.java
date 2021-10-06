package com.epam.jwd.apotheca.model;

public class Drug implements Entity, Comparable {

	private Integer id;
	private String name;
	private Integer quantity;
	private Integer price;
	private Double dose;
	private Boolean prescription;

	@Override
	public Integer getId() {
		return id;
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

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public boolean isPrescription() {
		return prescription;
	}

	public void setPrescription(boolean prescription) {
		this.prescription = prescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dose == null) ? 0 : dose.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((prescription == null) ? 0 : prescription.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Drug other = (Drug) obj;
		if (dose == null) {
			if (other.dose != null)
				return false;
		} else if (!dose.equals(other.dose))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prescription == null) {
			if (other.prescription != null)
				return false;
		} else if (!prescription.equals(other.prescription))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Drug [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", dose=" + dose
				+ ", prescription=" + prescription + "]";
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int compareTo(Object o) {
		if ( o == null ) {
			return 1;
		}
		if ( o.equals(this) ) {
			return 0;
		}
		if (getClass() != o.getClass() ) {
			throw new IllegalArgumentException("Object is not a drug!");
		}
		if ( id == null ) {
			if ( ((Drug)o).id == null) {
				return 0;
			} else {
				return -1;
			}
		} 
		if ( ((Drug)o).id == null) {
			return 1;
		}
		
		
		return id.compareTo(((Drug)o).id) ;
		
	}

}
