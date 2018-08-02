package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;


public class Location implements Serializable {

	private static final long serialVersionUID = 8757015453397191318L;

	private Long idLocation;
	private String city;
	private String complement;
	private String name;
	private String partnerLocationCode;
	private String state;
	private String address;
	private String neighborhood;
	private String number;
	private String zipCode;
	private Customer customer;

	private Double latitude;
	private Double longitude;
	
	public Location() {
	}

	public Long getIdLocation() {
		return this.idLocation;
	}

	public void setIdLocation(Long idLocation) {
		this.idLocation = idLocation;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComplement() {
		return this.complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerLocationCode() {
		return this.partnerLocationCode;
	}

	public void setPartnerLocationCode(String partnerLocationCode) {
		this.partnerLocationCode = partnerLocationCode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNeighborhood() {
		return this.neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood= neighborhood;
	}
	
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

}