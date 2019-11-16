package com.smart.farm.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	private String roleName;
	
	/**
	 * @param id
	 * @param roleName
	 */
	public AppRole(long id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}

	/**
	 * 
	 */
	public AppRole() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	} 
}
