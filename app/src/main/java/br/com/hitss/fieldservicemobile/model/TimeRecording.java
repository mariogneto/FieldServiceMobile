package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeRecording implements Serializable {

	private static final long serialVersionUID = 5701472845482507687L;

	private Long idTimeRecording;
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", locale="pt-BR", timezone="America/Sao_Paulo")
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