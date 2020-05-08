package org.tess.automation.dao;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;

@Entity
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;

	private String groupName;

	private String type;

	private String defaultValue;

	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private Node connectedNode;

	private String connectorSlot;

	@MapKeyColumn(name = "request_type")
	@ElementCollection(targetClass = String.class)
	private Map<String, String> urlMap;

}
