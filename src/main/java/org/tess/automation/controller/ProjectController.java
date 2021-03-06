package org.tess.automation.controller;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RelationNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
import org.tess.automation.dao.Project;
import org.tess.automation.repository.ProjectRepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/meta/project")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = {"*"})
public class ProjectController {

	@Autowired
	ProjectRepo projectRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> insertProject(@RequestBody Project project) throws JsonMappingException, JsonProcessingException {
		Project pro = null;
		try{
			 pro = projectRepo.save(project);
		}catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Duplicate Project Name - "+ project.getName());
		}
		return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(pro));
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<Optional<Project>> getProject(@PathVariable("projectId") long projectId) {
		Optional<Project> res = projectRepo.findById(projectId);
		if (! res.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Project>> getProject() {
		 List<Project> res = projectRepo.findAll();
		if (res.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(res);
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
		project.setName(updatedNode.getName());

		Project updatedNote = projectRepo.save(project);
		return updatedNote;
	}

}
