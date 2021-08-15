package com.epam.jwd.apotheca.model;

import java.sql.Date;
import java.util.List;

public class Recipe implements Entity {

	private Integer id;
	private Integer userId;
	private List<Integer> drugIds;
	private Integer doctorId;
	private Date expieryDate;

	public Integer getId() {
		return id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Integer> getDrugIds() {
		return drugIds;
	}

	public void setDrugIds(List<Integer> drugIds) {
		this.drugIds = drugIds;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Date getExpieryDate() {
		return expieryDate;
	}

	public void setExpieryDate(Date expieryDate) {
		this.expieryDate = expieryDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doctorId == null) ? 0 : doctorId.hashCode());
		result = prime * result + ((drugIds == null) ? 0 : drugIds.hashCode());
		result = prime * result + ((expieryDate == null) ? 0 : expieryDate.hashCode());
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
		Recipe other = (Recipe) obj;
		if (doctorId == null) {
			if (other.doctorId != null)
				return false;
		} else if (!doctorId.equals(other.doctorId))
			return false;
		if (drugIds == null) {
			if (other.drugIds != null)
				return false;
		} else if (!drugIds.equals(other.drugIds))
			return false;
		if (expieryDate == null) {
			if (other.expieryDate != null)
				return false;
		} else if (!expieryDate.equals(other.expieryDate))
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
		return "Recipe [id=" + id + ", userId=" + userId + ", drugIds=" + drugIds + ", doctorId=" + doctorId
				+ ", expieryDate=" + expieryDate + "]";
	}

}
