package org.tess.automation.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tess.automation.dao.Device;
import org.tess.automation.dao.http.ElectraDevice;
import org.tess.automation.dao.http.UserHTTPRequest;
import org.tess.automation.repository.DeviceRepo;
import org.tess.automation.util.ConnectionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/control")
@CrossOrigin(origins = "http://localhost:3000")
public class OperationController {

	@Autowired
	DeviceRepo deviceRepo;

	ConnectionHandler handler;

	@PostMapping("/switch")
	public void triggerSwitch(@RequestBody UserHTTPRequest request)
			throws JsonMappingException, JsonProcessingException, URISyntaxException {

		ElectraDevice reqDevice = request.getDevice();
		Device triggerDevice = deviceRepo.findByProjectAndGroupNameAndName(reqDevice.getProject(),
				reqDevice.getGroupName(), reqDevice.getDevice());
		triggerDevice.setStatus(request.getValue());
		Device dev = deviceRepo.save(triggerDevice);
		System.out.println(request + "request Accepted and Updated the DB");
		
		String reqAppender = "analogOutput";
		if(request.getIsDigital()) {
			reqAppender = "output";
		}
		
		new ConnectionHandler().callToNode(triggerDevice.getConnectedNode().getIp(), triggerDevice.getConnectedNode().getPort(),
				triggerDevice.getConnectorSlot(), request.getValue(), reqAppender);
		System.out.println("Command Send to the Node Device");
	}

	@PostMapping("/sensor")
	public void triggerSensor(@RequestBody UserHTTPRequest request)
			throws JsonMappingException, JsonProcessingException {

		ElectraDevice reqDevice = request.getDevice();
		Device triggerDevice = deviceRepo.findByProjectAndGroupNameAndName(reqDevice.getProject(),
				reqDevice.getGroupName(), reqDevice.getDevice());

		System.out.println(triggerDevice);
	}

}
