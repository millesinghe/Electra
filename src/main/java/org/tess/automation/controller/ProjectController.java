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
import org.tess.automation.dao.Project;
import org.tess.automation.repository.ProjectRepo;

@RestController
@RequestMapping("/meta/project")
public class ProjectController {
	
	@Autowired
	ProjectRepo projectRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertProject(@RequestBody Project project) {
		projectRepo.save(project);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<Optional<Project>> getProject(@PathVariable("projectId") long projectId) {
		return ResponseEntity.ok().body(projectRepo.findById(projectId));
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<Object> deleteProject(@PathVariable("projectId") long projectId) {
		projectRepo.deleteById(projectId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("byName/{projectName}")
	public ResponseEntity<Project> getProjectByName(@PathVariable("projectName") String name) {
		return ResponseEntity.ok().body(projectRepo.findByName(name));
	}

}
