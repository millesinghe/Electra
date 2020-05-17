package org.tess.automation.dao;

import java.util.List;

public class ProjectNode {
	
	private int projectid;
	
	private List<Node> nodes;

	public int getProjectid() {
		return projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	
}
