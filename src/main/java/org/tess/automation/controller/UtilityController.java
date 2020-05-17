package org.tess.automation.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.tess.automation.dao.AutomataComponent;
import org.tess.automation.dao.Node;
import org.tess.automation.dao.Project;
import org.tess.automation.repository.ProjectRepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/manage")
public class UtilityController {

	private static int NODE_STANDERD_PORT = 5000;

	@Autowired
	ProjectRepo projectRepo;

	private int nodeLimit;

	private Map mapNodes;

	private List<Node> availableNodes;
	
	private List<Node> removeNodes;

	@GetMapping("scan/{projectName}")
	public ResponseEntity<String> checkConnection(@PathVariable("projectName") String projectName) {

		setMapNodes(new HashMap<>());
		
		Project pro = projectRepo.findByName(projectName);
		nodeLimit = pro.getNodesList().size();

		InetAddress localhost = null;
		this.scanIPs(projectName);

		String body = this.getMapNodes().toString();
		ResponseEntity<String> response = new ResponseEntity<String>(body, HttpStatus.FOUND);
		System.out.println("Have - " +this.getAvailableNodes().get(0).getIp());
		this.getRemoveNodes();
		return response;
	}

	private void scanIPs(String projectName) {
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();

			// this code assumes IPv4 is used
			byte[] ip = localhost.getAddress();

			System.out.println("Connected Devices and Machines to the Network");

			for (int i = 1; i <= 254; i++) {
				ip[3] = (byte) i;
				InetAddress address = InetAddress.getByAddress(ip);
				System.out.println(address);

				if (nodeLimit == 0) {
					break;
				}
				if (address.isReachable(500)) {
					String serverUrl = "http://" + address.getHostAddress() + ":" + NODE_STANDERD_PORT + "/ping";
					try {
						this.checkNodeElectra(serverUrl);
						// this.filterNodeURL(address);
					} catch (Exception e) {
						continue;
					}

				}
			}
			Project pro = projectRepo.findByName(projectName);
			this.updateIps(pro);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void updateIps(Project project) {
		Map scannedNodeList = this.mapNodes;
		List<Node> prevProjectNodes = project.getNodesList();

		availableNodes = new ArrayList<>();
		removeNodes = new ArrayList<>();
		
		for (Node node : prevProjectNodes) {
			if (scannedNodeList.get(String.valueOf(node.getId().toString())) != null) {
				String scannedIP = scannedNodeList.get(String.valueOf(node.getId().toString())).toString()
						.split(":")[0];
				String prevIp = node.getIp();
				if (!prevIp.equals(scannedNodeList.get(node.getId()))) {
					node.setIp(scannedIP);
					availableNodes.add(node);		
				}
			}else {
				removeNodes.add(node);
			}
		}
		project.setNodesList(availableNodes);
		projectRepo.save(project);
	}

	private void checkNodeElectra(String baseUrl) throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

		AutomataComponent obj = null;
		try {
			obj = new ObjectMapper().readValue(result.getBody(), AutomataComponent.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapNodes.put(obj.getId(), uri.getAuthority());
		nodeLimit--;
		System.out.println();

	}

	public Map getMapNodes() {
		return mapNodes;
	}

	public void setMapNodes(Map mapNodes) {
		this.mapNodes = mapNodes;
	}

	public List<Node> getAvailableNodes() {
		return availableNodes;
	}

	public List<Node> getRemoveNodes() {
		return removeNodes;
	}

}
