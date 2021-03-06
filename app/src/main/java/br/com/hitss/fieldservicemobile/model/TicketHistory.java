package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;
import java.util.Date;


public class TicketHistory implements Serializable {

	private static final long serialVersionUID = 5701472845482507687L;

	private Long idTicketHistory;
	private Date dateTimeCreation;
	private String note;
	private Long idTicket;
	private Long idTicketStatus;
	private Long idUserExecutor;
	private Long idUserTechnician;
	private String integrationError;

	private String partnerTicketCode;
	private String partnerUserCode;
	private String status;

	public TicketHistory() {
	}

	public TicketHistory(TicketHistory ticketHistory) {
		this.setIdTicket(ticketHistory.getIdTicket());
		this.setIdUserTechnician(ticketHistory.getIdUserTechnician());
		this.setIdUserExecutor(ticketHistory.getIdUserExecutor());
		this.setStatus(ticketHistory.getStatus());
		this.setNote(ticketHistory.getNote());
	}

    public TicketHistory(Long idTicket,Long idUserTechnician, Long idUserExecutor, String status, String note) {
        this.setIdTicket(idTicket);
        this.setIdUserTechnician(idUserTechnician);
        this.setIdUserExecutor(idUserExecutor);
        this.setStatus(status);
        this.setNote(note);
    }

	public Long getIdTicketHistory() {
		return this.idTicketHistory;
	}

	public void setIdTicketHistory(Long idTicketHistory) {
		this.idTicketHistory = idTicketHistory;
	}

	public Date getDateTimeCreation() {
		return this.dateTimeCreation;
	}

	public void setDateTimeCreation(Date dateTimeCreation) {
		this.dateTimeCreation = dateTimeCreation;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getIdTicket() {
		return this.idTicket;
	}

	public void setIdTicket(Long idTicket) {
		this.idTicket = idTicket;
	}

	public Long getIdTicketStatus() {
		return this.idTicketStatus;
	}

	public void setIdTicketStatus(Long idTicketStatus) {
		this.idTicketStatus = idTicketStatus;
	}

	public Long getIdUserExecutor() {
		return idUserExecutor;
	}

	public void setIdUserExecutor(Long idUserExecutor) {
		this.idUserExecutor = idUserExecutor;
	}

	public Long getIdUserTechnician() {
		return idUserTechnician;
	}

	public void setIdUserTechnician(Long idUserTechnician) {
		this.idUserTechnician = idUserTechnician;
	}

	public String getIntegrationError() {
		return integrationError;
	}

	public void setIntegrationError(String integrationError) {
		this.integrationError = integrationError;
	}

	public String getPartnerTicketCode() {
		return partnerTicketCode;
	}

	public void setPartnerTicketCode(String partnerTicketCode) {
		this.partnerTicketCode = partnerTicketCode;
	}

	public String getPartnerUserCode() {
		return partnerUserCode;
	}

	public void setPartnerUserCode(String partnerUserCode) {
		this.partnerUserCode = partnerUserCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}