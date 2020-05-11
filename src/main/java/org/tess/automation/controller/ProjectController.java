package org.tess.automation.controller;

import java.util.Optional;

import javax.management.relation.RelationNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tess.automation.dao.Node;
import org.tess.automation.dao.Project;
import org.tess.automation.repository.ProjectRepo;

@RestController
@RequestMapping("/meta/project")
public class ProjectController {
	
	@Autowired
	ProjectRepo projectRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Project insertProject(@RequestBody Project project) {
		return projectRepo.save(project);
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
	
	@PutMapping("/{projectId}")
	public Project updateProject(@PathVariable("projectId") Long projectId, @Valid @RequestBody Project updatedNode) {

		Project project = null;
		try {
			project = projectRepo.findById(projectId)
					.orElseThrow(() -> new RelationNotFoundException(projectId.toString()));
		} catch (RelationNotFoundException e) {
			e.printStackTrace();
		}

		project.setConnection_SSID(updatedNode.getConnection_SSID());
		project.setConnection_Password(updatedNode.getConnection_Password());
		project.setIp(updatedNode.getIp());
		project.setName(updatedNode.getName());
		project.setNodesCount(updatedNode.getNodesCount());
		
		Project updatedNote = projectRepo.save(project);
		return updatedNote;
	}

}
