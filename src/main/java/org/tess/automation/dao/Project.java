package org.tess.automation.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String connection_SSID;

	private String connection_Password;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Node> nodesList;

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

	public List<Node> getNodesList() {
		return nodesList;
	}

	public void setNodesList(List<Node> nodesList) {
		this.nodesList = nodesList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
