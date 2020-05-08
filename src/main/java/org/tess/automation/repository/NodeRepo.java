package org.tess.automation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tess.automation.dao.Node;

public interface NodeRepo extends JpaRepository<Node, Long> {

	Node findByIp(String nodeIp);

}
