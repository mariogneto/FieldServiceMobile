package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile implements Serializable {

	private static final long serialVersionUID = -3779418468797129627L;

	private Long idProfile;
	private String description;
	private String name;
	private List<Role> roles;

	public Profile() {
	}

	public Long getIdProfile() {
		return this.idProfile;
	}

	public void setIdProfile(Long idProfile) {
		this.idProfile = idProfile;
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

	public List<Role> getRoles() {
		if (this.roles.isEmpty()) {
			return null;
		} else {
			return this.roles;
		}
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}