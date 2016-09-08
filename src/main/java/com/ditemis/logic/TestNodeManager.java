package com.ditemis.logic;

import com.ditemis.rs.TestService;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.UUID;

@RequestScoped
public class TestNodeManager {

    private static final Logger LOGGER = Logger.getLogger(TestService.class);

    @Inject
    private Session session;
    
    private final int CHILD_TEST_NODES = 5;
    
    public String buildTestNode() throws RepositoryException {
//        LOGGER.info("buildTestNode");
        try {
            Node testNode = session.getRootNode().getNode("test");
            Node newNode = testNode.addNode(UUID.randomUUID().toString());
            newNode.setProperty("status", "new");
            
            for (int i = 0; i < CHILD_TEST_NODES; i++) {
                Node childNode = newNode.addNode("child" + i);
                childNode.setProperty("test", "test");
            }

            session.save();

            Node getNewNode = session.getNodeByIdentifier(newNode.getIdentifier());
            getNewNode.getName();
            return getNewNode.getIdentifier();
        } catch (RepositoryException rex) {
            LOGGER.info("Failed building new node", rex);
            throw rex;
        }
    }
    
    public String updateTestNode(String nodeId, String status) throws RepositoryException {
//        LOGGER.info("updateTestNode " + nodeId);
        try {
            Node testNode = session.getNodeByIdentifier(nodeId);
            testNode.setProperty("status", status);

            // only update one child node
            Node childNode = testNode.getNode("child0");
            childNode.setProperty("test", status);

            session.save();

            return testNode.getIdentifier();
        } catch (RepositoryException rex) {
            LOGGER.info("Failed updating node " + nodeId, rex);
            throw rex;
        }
    }

    public String deleteTestNode(String nodeId) throws RepositoryException {
        try {
            Node testNode = session.getNodeByIdentifier(nodeId);
            String nodeIdentifier = testNode.getIdentifier();
            testNode.remove();

            session.save();
            
            return nodeIdentifier;
        } catch (RepositoryException rex) {
            LOGGER.info("Failed deleting node " + nodeId, rex);
            throw rex;
        }
    }
}
