package org.tess.automation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tess.automation.dao.Device;

public interface DeviceRepo extends JpaRepository<Device, Long> {

	Device findByName(String deviceName);

	Device findByProjectAndGroupNameAndName(String project, String groupName, String name);

}
