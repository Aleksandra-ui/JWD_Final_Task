package com.epam.jwd.apotheca.model;

public class Role implements Entity {

	private Integer id;
	private String name;
	private Integer permission;

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

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", permission=" + permission + "]";
	}
	
}
