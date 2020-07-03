package org.tess.automation.dao;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Device implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String groupName;

	private String type;

	private String defaultValue;

	private String status;

	private String project;

	public Device() {
		// TODO Auto-generated constructor stub
	}
	
	public Device(long deviceId) {
		this.id = deviceId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	@ManyToOne
    @JoinColumn(name = "node_id")
	@JsonBackReference
	private Node connectedNode;

	private String connectorSlot;

	@MapKeyColumn(name = "request_type")
	@ElementCollection(targetClass = String.class)
	private Map<String, String> urlMap;

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Node getConnectedNode() {
		return connectedNode;
	}

	public void setConnectedNode(Node connectedNode) {
		this.connectedNode = connectedNode;
	}

	public String getConnectorSlot() {
		return connectorSlot;
	}

	public void setConnectorSlot(String connectorSlot) {
		this.connectorSlot = connectorSlot;
	}

	public Map<String, String> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
