package org.tess.automation.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String connection_SSID;

	private String connection_Password;

	private String ip;
	
	private int nodesCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConnection_SSID() {
		return connection_SSID;
	}

	public void setConnection_SSID(String connection_SSID) {
		this.connection_SSID = connection_SSID;
	}

	public String getConnection_Password() {
		return connection_Password;
	}

	public void setConnection_Password(String connection_Password) {
		this.connection_Password = connection_Password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getNodesCount() {
		return nodesCount;
	}

	public void setNodesCount(int nodesCount) {
		this.nodesCount = nodesCount;
	}

}
