package com.ditemis.logic;

import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.jboss.logging.Logger;

import com.ditemis.rs.TestService;

@RequestScoped
public class TestNodeManager {

    private static final Logger LOGGER = Logger.getLogger(TestService.class);

    @Inject
    private Session session;
    
    private final int CHILD_TEST_NODES = 5;
    private final int SUBCHILD_TEST_NODES = 5;
    
    public String buildTestNode() throws RepositoryException {
//        LOGGER.info("buildTestNode");
        try {
            Node testNode = session.getRootNode().getNode("test");
            Node newNode = testNode.addNode(UUID.randomUUID().toString());
            newNode.setProperty("status", "new");

            Random r = new Random();
            String type = "type" + (r.nextInt(5) + 1);
            
            String clientId = UUID.randomUUID().toString();
            newNode.setProperty("clientId", clientId);
            newNode.setProperty("type", type);
            
//            for (int i = 0; i < CHILD_TEST_NODES; i++) {
//                Node childNode = newNode.addNode("child" + i);
//                childNode.setProperty("test", "test");
//            }

            session.save();

            Node getNewNode = session.getNode(newNode.getPath());
            
            return "{ \"nodeId\":\"" + getNewNode.getIdentifier() + "\", \"clientId\":\"" + clientId +"\", \"type\":\"" + type + "\"}";
        } catch (RepositoryException rex) {
            LOGGER.info("Failed building new node", rex);
            throw rex;
        }
    }
    
    public String buildTestNodeWithTypeStructure() throws RepositoryException {
//        LOGGER.info("buildTestNode");
        try {
            Random r = new Random();
            String type = "type" + (r.nextInt(5) + 1);
            
            Node testNode = session.getRootNode().getNode("test");
            Node typeNode = testNode.getNode(type);
            Node newNode = typeNode.addNode(UUID.randomUUID().toString());
            newNode.setProperty("status", "new");
            
            String clientId = UUID.randomUUID().toString();
            newNode.setProperty("clientId", clientId);
            newNode.setProperty("type", type);
            
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
    
    public String getTestNode(String clientId, String type) throws RepositoryException {
//      LOGGER.info("getTestNode");
      try {
          // TODO search with sql
          javax.jcr.query.QueryManager queryManager = session.getWorkspace().getQueryManager();
          String language = "JCR-SQL2";
          StringBuilder sql = new StringBuilder("SELECT node.* FROM [nt:base] AS node ")
                  .append("WHERE ISCHILDNODE(['/test/'])")
                  .append(" AND clientId = '")
                  .append(clientId)
                  .append("' AND type = '")
                  .append(type)
                  .append("'");
          
          Query query = queryManager.createQuery(sql.toString(),language);
          query.setLimit(1);
          QueryResult result = query.execute();

          javax.jcr.NodeIterator nodeIter = result.getNodes();
          while (nodeIter.hasNext()) {
              javax.jcr.Node node = nodeIter.nextNode();
              return node.getProperty("clientId").getValue().toString();
          }
          throw new RepositoryException("Could not find node with clientId: " + clientId);
      } catch (RepositoryException rex) {
          LOGGER.info("Failed retrieving new node", rex);
          throw rex;
      }
  }
    
    public String getTestNodeWithTypeStructure(String clientId, String type) throws RepositoryException {
//      LOGGER.info("getTestNode");
      try {
          javax.jcr.query.QueryManager queryManager = session.getWorkspace().getQueryManager();
          String language = "JCR-SQL2";
          StringBuilder sql = new StringBuilder("SELECT node.* FROM [nt:base] AS node ")
                  .append("WHERE ISCHILDNODE(['/test/" + type + "/'])")
                  .append(" AND clientId = '")
                  .append(clientId)
                  .append("'");
          
          Query query = queryManager.createQuery(sql.toString(),language);
          query.setLimit(1);
          QueryResult result = query.execute();

          javax.jcr.NodeIterator nodeIter = result.getNodes();
          while (nodeIter.hasNext()) {
              javax.jcr.Node node = nodeIter.nextNode();
              return node.getProperty("clientId").getValue().toString();
          }
          throw new RepositoryException("Could not find node with clientId: " + clientId);
      } catch (RepositoryException rex) {
          LOGGER.info("Failed retrieving new node", rex);
          throw rex;
      }
  }
    public String getTestNodeWithTypeStructureOnlyWithId(String clientId, String type) throws RepositoryException {
//      LOGGER.info("getTestNode");
      try {
          javax.jcr.query.QueryManager queryManager = session.getWorkspace().getQueryManager();
          String language = "JCR-SQL2";
          for (int i = 0; i < 5; i++) {
              StringBuilder sql = new StringBuilder("SELECT node.* FROM [nt:base] AS node ")
                  .append("WHERE ISCHILDNODE(['/test/type" + (i+1) + "/'])")
                  .append(" AND clientId = '")
                  .append(clientId)
                  .append("'");
              
              Query query = queryManager.createQuery(sql.toString(),language);
              query.setLimit(1);
              QueryResult result = query.execute();
    
              javax.jcr.NodeIterator nodeIter = result.getNodes();
              while (nodeIter.hasNext()) {
                  javax.jcr.Node node = nodeIter.nextNode();
                  return node.getProperty("clientId").getValue().toString();
              }
          }
          throw new RepositoryException("Could not find node with clientId: " + clientId);
      } catch (RepositoryException rex) {
          LOGGER.info("Failed retrieving new node", rex);
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
