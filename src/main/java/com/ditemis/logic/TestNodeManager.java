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
    
    public String buildTestNode() {
        // TODO create node
        LOGGER.info("newTestNode");
        try {
            Node rootNode = session.getRootNode();
            Node newNode = rootNode.addNode(UUID.randomUUID().toString());
            
            session.save();
            return newNode.getIdentifier();
        } catch (RepositoryException rex) {
            LOGGER.info("Failed building new node", rex);
        }
        return null;
    }
}
