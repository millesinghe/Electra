package org.tess.automation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tess.automation.dao.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {

	Project findByName(String name);

}
