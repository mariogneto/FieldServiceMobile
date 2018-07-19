package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Supplier implements Serializable {

	private static final long serialVersionUID = 3609034588681961855L;

	private Long idSupplier;
	private String description;
	private String name;

	public Supplier() {
	}

	public Long getIdSupplier() {
		return this.idSupplier;
	}

	public void setIdSupplier(Long idSupplier) {
		this.idSupplier = idSupplier;
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