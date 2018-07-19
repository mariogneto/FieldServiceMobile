package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {

	private static final long serialVersionUID = 2564260395404739437L;

	private Long idRole;
	private String description;
	private String name;

	public Role() {
	}

	public Long getIdRole() {
		return this.idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}