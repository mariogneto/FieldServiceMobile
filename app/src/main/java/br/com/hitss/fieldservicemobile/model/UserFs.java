package br.com.hitss.fieldservicemobile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFs implements Serializable {

	private static final long serialVersionUID = -8502688384310900348L;

	private Long idUserFs;
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", locale="pt-BR", timezone="America/Sao_Paulo")
	private Date dateTimeAvailable;
	private String fullName;
	private String login;
	private String password;
	private String numDocument;
	private String partnerUserCode;
	private String phone;
	private String status;

	private Location location;

	private Profile profile;

	private Supplier supplier;

	private UserLocationHistory userLocationHistory;
	
	private Collection<TimeRecording> timeRecordings;
	
	public UserFs() {
	}
	
	public UserFs(Long idUserFs) {
		this.idUserFs = idUserFs;
	}

	public Long getIdUserFs() {
		return this.idUserFs;
	}

	public void setIdUserFs(Long idUserFs) {
		this.idUserFs = idUserFs;
	}

	public Date getDateTimeAvailable() {
		return this.dateTimeAvailable;
	}

	public void setDateTimeAvailable(Date dateTimeAvailable) {
		this.dateTimeAvailable = dateTimeAvailable;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNumDocument() {
		return this.numDocument;
	}

	public void setNumDocument(String numDocument) {
		this.numDocument = numDocument;
	}

	public String getPartnerUserCode() {
		return this.partnerUserCode;
	}

	public void setPartnerUserCode(String partnerUserCode) {
		this.partnerUserCode = partnerUserCode;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Profile getProfile() {
		return this.profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Supplier getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public UserLocationHistory getUserLocationHistory() {
		return this.userLocationHistory;
	}

	public void setUserLocationHistory(UserLocationHistory userLocationHistory) {
		this.userLocationHistory = userLocationHistory;
	}

	public Collection<TimeRecording> getTimeRecordings() {
		return timeRecordings;
	}

	public void setTimeRecordings(Collection<TimeRecording> timeRecordings) {
		this.timeRecordings = timeRecordings;
	}

	public void addTimeRecordings(TimeRecording timeRcording) {
		if (this.timeRecordings == null) {
			this.timeRecordings = new ArrayList<>();
		}
		this.timeRecordings.add(timeRcording);
	}
}