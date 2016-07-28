package com.ditemis.logic;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jboss.logging.Logger;

import com.ditemis.rs.TestService;

@RequestScoped
public class TestNodeManager {

    private static final Logger LOGGER = Logger.getLogger(TestService.class);

    @Inject
    private Session session;
    
    public String buildTestNode() throws RepositoryException {
        LOGGER.info("buildTestNode");
        try {
            Node rootNode = session.getRootNode();
            Node newNode = rootNode.addNode("test/" + UUID.randomUUID().toString());
            newNode.setProperty("status", "new");
            
            Node childNode1 = newNode.addNode("child1");
            childNode1.setProperty("test", "test");
            
            Node childNode2 = newNode.addNode("child2");
            childNode2.setProperty("test", "test");
            
            Node childNode3 = newNode.addNode("child3");
            childNode3.setProperty("test", "test");
            
            Node childNode4 = newNode.addNode("child4");
            childNode4.setProperty("test", "test");
            
            Node childNode5 = newNode.addNode("child5");
            childNode5.setProperty("test", "test");
            
            session.save();
            
            Node getNewNode = session.getNodeByIdentifier(newNode.getIdentifier());
            return getNewNode.getIdentifier();
        } catch (RepositoryException rex) {
            LOGGER.info("Failed building new node", rex);
            throw rex;
        }
    }
    
    public String updateTestNode(String nodeId, String status) throws RepositoryException {
        LOGGER.info("updateTestNode " + nodeId);
        try {
            Node testNode = session.getNodeByIdentifier(nodeId);
            testNode.setProperty("status", status);
            
            session.save();
            
            return testNode.getIdentifier();
        } catch (RepositoryException rex) {
            LOGGER.info("Failed updating node " + nodeId, rex);
            throw rex;
        }
    }
}
