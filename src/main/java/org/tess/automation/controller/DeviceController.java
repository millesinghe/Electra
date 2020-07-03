package org.tess.automation.controller;

import java.util.Optional;

import javax.management.relation.RelationNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tess.automation.dao.Device;
import org.tess.automation.repository.DeviceRepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/meta/device")
@CrossOrigin(origins = {"*"})
public class DeviceController {

	@Autowired
	DeviceRepo deviceRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Device insertDevice(@RequestBody Device device) throws JsonMappingException, JsonProcessingException {
		device.setStatus(device.getDefaultValue());
		Device dev = deviceRepo.save(device);
		return dev;
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<Optional<Device>> getDevice(@PathVariable("deviceId") long deviceId) {
		return ResponseEntity.ok().body(deviceRepo.findById(deviceId));
	}

	@DeleteMapping("/{deviceId}")
	public Device deleteDevice(@PathVariable("deviceId") long deviceId) {
		deviceRepo.deleteById(deviceId);
		return new Device(deviceId);
	}

	@GetMapping("byName/{projectName}/{groupName}/{deviceName}")
	public ResponseEntity<Device> getDeviceByName(@PathVariable("projectName") String projectName,
			@PathVariable("groupName") String groupName, @PathVariable("deviceName") String deviceName) {
				//return null;
		return ResponseEntity.ok().body(deviceRepo.findByProjectAndGroupNameAndName(projectName, groupName, deviceName));
	}

	@PutMapping("/{deviceId}")
	public Device updateDevice(@PathVariable("deviceId") Long deviceId, @Valid @RequestBody Device updatedDevice) {

		Device device = null;
		try {
			device = deviceRepo.findById(deviceId)
					.orElseThrow(() -> new RelationNotFoundException(deviceId.toString()));
		} catch (RelationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		device.setConnectedNode(updatedDevice.getConnectedNode());
		device.setConnectorSlot(updatedDevice.getConnectorSlot());
		device.setDefaultValue(updatedDevice.getDefaultValue());
		device.setStatus(updatedDevice.getDefaultValue());
		device.setGroupName(updatedDevice.getGroupName());
		device.setName(updatedDevice.getName());
		device.setProject(updatedDevice.getProject());
		device.setType(updatedDevice.getType());
		device.setUrlMap(updatedDevice.getUrlMap());
		
		Device retDevice = deviceRepo.save(device);
		return retDevice;
	}
}
