package org.tess.automation.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tess.automation.dao.AutomataComponent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionListner {

	private static int CONTINUE_FAIL_THREASOLD = 5;

	private List<AutomataComponent> nodeList;

	public List<AutomataComponent> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<AutomataComponent> nodeList) {
		this.nodeList = nodeList;
	}

	int continuesFails;

	public static void main(String[] args) throws IOException {
		ConnectionListner connection = new ConnectionListner();
		connection.checkConnection();
		connection.getNodeList();
		System.out.println("");
	}

	private void checkConnection() {

		nodeList = new ArrayList();
		
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();

			// this code assumes IPv4 is used
			byte[] ip = localhost.getAddress();

			System.out.println("Connected Devices and Machines to the Network");
			continuesFails = 0;
			for (int i = 1; i <= 254; i++) {
				ip[3] = (byte) i;
				InetAddress address = InetAddress.getByAddress(ip);
				if (address.isReachable(500)) {
					String serverUrl = "http://" + address.getHostAddress() + ":8081/isAutomataNode";
					try {
						this.checkNodeMCU(serverUrl);
						//this.filterNodeURL(address);
					} catch (Exception e) {
						if (CONTINUE_FAIL_THREASOLD <= continuesFails)
							return;
						continuesFails++;
						continue;
					}

				} else {
					continuesFails++;
				}
//			else if (!address.getHostAddress().equals(address.getHostName())) {
//				System.out.println(address + " machine is known in a DNS lookup");
//			} else {
//				System.out.println(address
//						+ " the host address and host name are equal, meaning the host name could not be resolved");
//			}
				if (CONTINUE_FAIL_THREASOLD <= continuesFails)
					return;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void checkNodeMCU(String baseUrl) throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
	     
	    URI uri = new URI(baseUrl);
	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
	     System.out.println();
	}

	private void filterNodeURL(InetAddress address) throws IOException {
		URL url = null;

		url = new URL("http://" + address.getHostAddress() + ":8081/isAutomataNode");
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String response = IOUtils.toString(in, encoding);
		ObjectMapper mapper = new ObjectMapper();
		AutomataComponent node = mapper.readValue(response, AutomataComponent.class);
		nodeList.add(node);
		continuesFails = 0;

	}

}
