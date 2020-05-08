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
import org.tess.automation.dao.Node;
import org.tess.automation.repository.NodeRepo;

@RestController
@RequestMapping("/meta/node")
public class NodeController {

	@Autowired
	NodeRepo nodeRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertNode(@RequestBody Node node) {
		nodeRepo.save(node);
	}

	@GetMapping("/{nodeId}")
	public ResponseEntity<Optional<Node>> getNode(@PathVariable("nodeId") long nodeId) {
		return ResponseEntity.ok().body(nodeRepo.findById(nodeId));
	}

	@DeleteMapping("/{nodeId}")
	public void deleteNode(@PathVariable("nodeId") long nodeId) {
		nodeRepo.deleteById(nodeId);
	}

	@GetMapping("byName/{nodeIp}")
	public ResponseEntity<Node> getNodeByIp(@PathVariable("nodeIp") String nodeIp) {
		return ResponseEntity.ok().body(nodeRepo.findByIp(nodeIp));
	}

}
