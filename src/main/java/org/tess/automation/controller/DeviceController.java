package org.tess.automation.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tess.automation.dao.Device;
import org.tess.automation.repository.DeviceRepo;

@RestController
@RequestMapping("/meta/device")
public class DeviceController {

	@Autowired
	DeviceRepo deviceRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertDevice(@RequestBody Device device) {
		deviceRepo.save(device);
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<Optional<Device>> getDevice(@PathVariable("deviceId") long deviceId) {
		return ResponseEntity.ok().body(deviceRepo.findById(deviceId));
	}

	@DeleteMapping("/byName/{deviceId}")
	public void deleteDevice(@PathVariable("deviceId") long deviceId) {
		deviceRepo.deleteById(deviceId);
	}

	@GetMapping("byName/{deviceName}")
	public ResponseEntity<Device> getDeviceByName(@PathVariable("deviceName") String deviceName) {
		return ResponseEntity.ok().body(deviceRepo.findByDeviceName(deviceName));
	}
}
