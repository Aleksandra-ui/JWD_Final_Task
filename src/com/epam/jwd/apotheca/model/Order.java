package com.epam.jwd.apotheca.model;

import java.sql.Date;
import java.util.Map;

public class Order implements Entity {

	private Integer id;
	private Map<Drug, Integer> drugs;
	private Integer userId;
	private Date date;

	public Integer getId() {
		return id;
	}

	public Map<Drug, Integer> getDrugs() {
		return drugs;
	}

	public void setDrugs(Map<Drug, Integer> drugs) {
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

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((drugs == null) ? 0 : drugs.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;

	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (drugs == null) {
			if (other.drugs != null)
				return false;
		} else if (!drugs.equals(other.drugs))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;

	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", drugs=" + drugs + ", userId=" + userId + ", date=" + date + "]";
	}

}
