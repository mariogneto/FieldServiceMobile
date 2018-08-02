package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;
import java.util.Date;


public class TimeRecording implements Serializable {

	private static final long serialVersionUID = 5701472845482507687L;

	private Long idTimeRecording;
	private Date dateTimeCreation;
	private Long idUserFs;
	private String action;

	private String timeLogin;
	private String timeLogout;
	private String timeInterval;
	private String timeTotal;

	public Long getIdTimeRecording() {
		return idTimeRecording;
	}

	public void setIdTimeRecording(Long idTimeRecording) {
		this.idTimeRecording = idTimeRecording;
	}

	public Date getDateTimeCreation() {
		return dateTimeCreation;
	}

	public void setDateTimeCreation(Date dateTimeCreation) {
		this.dateTimeCreation = dateTimeCreation;
	}

	public Long getIdUserFs() {
		return idUserFs;
	}

	public void setIdUserFs(Long idUserFs) {
		this.idUserFs = idUserFs;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTimeLogin() {
		return timeLogin;
	}

	public void setTimeLogin(String timeLogin) {
		this.timeLogin = timeLogin;
	}

	public String getTimeLogout() {
		return timeLogout;
	}

	public void setTimeLogout(String timeLogout) {
		this.timeLogout = timeLogout;
	}

	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getTimeTotal() {
		return timeTotal;
	}

	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}
}