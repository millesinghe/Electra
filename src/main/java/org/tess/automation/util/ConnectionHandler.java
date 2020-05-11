package org.tess.automation.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class ConnectionHandler {

	public void callToNode(String ip, int port, String socket, String value, String triggerMode) {

		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://"+ip+":" + port +"/"+triggerMode +"/"+ socket +"/"+ value +"/";
	    URI uri = null;
	    
		try {
			uri = new URI(baseUrl);
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		} catch (ResourceAccessException e) {

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println();
//	    //Verify request succeed
//	    Assert.assertEquals(200, result.getStatusCodeValue());
//	    Assert.assertEquals(true, result.getBody().contains("employeeList"));
	}

	
	public void searchNodeList() {

	}

}
