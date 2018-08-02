package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;
import java.util.Date;


public class UserLocationHistory implements Serializable {

	private static final long serialVersionUID = -7429379053018724687L;

	private Long idUserLocationHistory;
	private Date dateTimeCreation;
	private Long idUser;

	private Double latitude;
	private Double longitude;
	private String fullName;
	
	public UserLocationHistory() {
	}

	public UserLocationHistory(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getIdUserLocationHistory() {
		return this.idUserLocationHistory;
	}

	public void setIdUserLocationHistory(Long idUserLocationHistory) {
		this.idUserLocationHistory = idUserLocationHistory;
	}

	public Date getDateTimeCreation() {
		return this.dateTimeCreation;
	}

	public void setDateTimeCreation(Date dateTimeCreation) {
		this.dateTimeCreation = dateTimeCreation;
	}

	public Long getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}