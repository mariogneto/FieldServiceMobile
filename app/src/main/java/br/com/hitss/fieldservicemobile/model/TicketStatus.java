package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketStatus implements Serializable {

	private static final long serialVersionUID = 7794225195417602323L;

	private Long idTicketStatus;
	private String description;
	private String name;

	public TicketStatus() {
	}
	
	public TicketStatus(Long idTicketStatus) {
		this.idTicketStatus = idTicketStatus;
	}

	public Long getIdTicketStatus() {
		return this.idTicketStatus;
	}

	public void setIdTicketStatus(Long idTicketStatus) {
		this.idTicketStatus = idTicketStatus;
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