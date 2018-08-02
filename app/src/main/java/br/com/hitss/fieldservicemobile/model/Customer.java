package br.com.hitss.fieldservicemobile.model;


import java.io.Serializable;


public class Customer implements Serializable {

	private static final long serialVersionUID = -3464337854050665283L;

	private Long idCustomer;
	private String name;
	private String partnerCustomerCode;

	public Customer() {
	}

	public Long getIdCustomer() {
		return this.idCustomer;
	}

	public void setIdCustomer(Long idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerCustomerCode() {
		return this.partnerCustomerCode;
	}

	public void setPartnerCustomerCode(String partnerCustomerCode) {
		this.partnerCustomerCode = partnerCustomerCode;
	}

}