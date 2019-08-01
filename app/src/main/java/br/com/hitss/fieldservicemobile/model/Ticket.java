package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Ticket implements Serializable {

	private static final long serialVersionUID = -2102390263672893118L;

	private Long idTicket;
	private Date dateScheduling;
	private Date dateTimeCreation;
	private Long effectiveResolution;
	private String partnerTicketCode;
	private String priority;
	private String problemDescription;
	private String problemLocalDetail;
	private Date serviceDeskDateTimeCreation;
	private String serviceDeskRequesterName;
	private String serviceDeskRequesterPhone;
	private String shiftScheduling;
	private Date sla;
	private String note;

	private TicketStatus ticketStatus;
	private UserFs userTechnician;

	private UserFs userAffected;
	
	private Date dateTimeEstimatedArrival;

	private List<TicketHistory> ticketHistories;
	
	public Ticket() {
	}

	public Long getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(Long idTicket) {
		this.idTicket = idTicket;
	}

	public Date getDateScheduling() {
		return dateScheduling;
	}

	public void setDateScheduling(Date dateScheduling) {
		this.dateScheduling = dateScheduling;
	}

	public Date getDateTimeCreation() {
		return dateTimeCreation;
	}

	public void setDateTimeCreation(Date dateTimeCreation) {
		this.dateTimeCreation = dateTimeCreation;
	}

	public Long getEffectiveResolution() {
		return effectiveResolution;
	}

	public void setEffectiveResolution(Long effectiveResolution) {
		this.effectiveResolution = effectiveResolution;
	}

	public String getPartnerTicketCode() {
		return partnerTicketCode;
	}

	public void setPartnerTicketCode(String partnerTicketCode) {
		this.partnerTicketCode = partnerTicketCode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getProblemLocalDetail() {
		return problemLocalDetail;
	}

	public void setProblemLocalDetail(String problemLocalDetail) {
		this.problemLocalDetail = problemLocalDetail;
	}

	public Date getServiceDeskDateTimeCreation() {
		return serviceDeskDateTimeCreation;
	}

	public void setServiceDeskDateTimeCreation(Date serviceDeskDateTimeCreation) {
		this.serviceDeskDateTimeCreation = serviceDeskDateTimeCreation;
	}

	public String getServiceDeskRequesterName() {
		return serviceDeskRequesterName;
	}

	public void setServiceDeskRequesterName(String serviceDeskRequesterName) {
		this.serviceDeskRequesterName = serviceDeskRequesterName;
	}

	public String getServiceDeskRequesterPhone() {
		return serviceDeskRequesterPhone;
	}

	public void setServiceDeskRequesterPhone(String serviceDeskRequesterPhone) {
		this.serviceDeskRequesterPhone = serviceDeskRequesterPhone;
	}

	public String getShiftScheduling() {
		return shiftScheduling;
	}

	public void setShiftScheduling(String shiftScheduling) {
		this.shiftScheduling = shiftScheduling;
	}

	public Date getSla() {
		return sla;
	}

	public void setSla(Date sla) {
		this.sla = sla;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public UserFs getUserTechnician() {
		return userTechnician;
	}

	public void setUserTechnician(UserFs userTechnician) {
		this.userTechnician = userTechnician;
	}

	public UserFs getUserAffected() {
		return userAffected;
	}

	public void setUserAffected(UserFs userAffected) {
		this.userAffected = userAffected;
	}

	public Date getDateTimeEstimatedArrival() {
		return dateTimeEstimatedArrival;
	}

	public void setDateTimeEstimatedArrival(Date dateTimeEstimatedArrival) {
		this.dateTimeEstimatedArrival = dateTimeEstimatedArrival;
	}

	public List<TicketHistory> getTicketHistories() {
		return ticketHistories;
	}

	public void setTicketHistories(List<TicketHistory> ticketHistories) {
		this.ticketHistories = ticketHistories;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}