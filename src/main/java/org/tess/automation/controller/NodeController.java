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
import org.tess.automation.dao.Node;
import org.tess.automation.repository.NodeRepo;

@RestController
@RequestMapping("/meta/node")
@CrossOrigin(origins = {"*"})
public class NodeController {

	@Autowired
	NodeRepo nodeRepo;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Node insertNode(@RequestBody Node node) {
		return nodeRepo.save(node);
	}

	@GetMapping("/{nodeId}")
	public ResponseEntity<Optional<Node>> getNode(@PathVariable("nodeId") long nodeId) {
		return ResponseEntity.ok().body(nodeRepo.findById(nodeId));
	}

	@DeleteMapping("/{nodeId}")
	public Node deleteNode(@PathVariable("nodeId") long nodeId) {
		nodeRepo.deleteById(nodeId);
		return new Node(nodeId);
	}

	@GetMapping("byName/{nodeIp}")
	public ResponseEntity<Node> getNodeByIp(@PathVariable("nodeIp") String nodeIp) {
		return ResponseEntity.ok().body(nodeRepo.findByIp(nodeIp));
	}

	@PutMapping("/{nodeId}")
	public Node updateNode(@PathVariable("nodeId") Long nodeId, @Valid @RequestBody Node updatedNode) {
		System.err.println("\n\n\n\nId - "+ updatedNode.getId()+"\n\n\n\n");
		Node node = null;
		try {
			node = nodeRepo.findById(nodeId)
					.orElseThrow(() -> new RelationNotFoundException(nodeId.toString()));
		} catch (RelationNotFoundException e) {
			e.printStackTrace();
		}

		node.setType(updatedNode.getType());
		node.setIp(updatedNode.getIp());
		node.setPort(updatedNode.getPort());
		node.setProject(updatedNode.getProject());
		Node updatedNote = nodeRepo.save(node);
		return updatedNote;
	}
}
