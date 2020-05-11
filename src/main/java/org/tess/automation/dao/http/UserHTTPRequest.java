package org.tess.automation.dao.http;

public class UserHTTPRequest {

	private ElectraAuth auth;
	
	private ElectraDevice device;
	
	private String value;
	
	private Boolean isDigital;

	public ElectraDevice getDevice() {
		return device;
	}

	public void setDevice(ElectraDevice device) {
		this.device = device;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ElectraAuth getAuth() {
		return auth;
	}

	public void setAuth(ElectraAuth auth) {
		this.auth = auth;
	}

	public Boolean getIsDigital() {
		return isDigital;
	}

	public void setIsDigital(Boolean isDigital) {
		this.isDigital = isDigital;
	}
	
}
