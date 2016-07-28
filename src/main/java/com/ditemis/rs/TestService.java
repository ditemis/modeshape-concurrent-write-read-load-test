package com.ditemis.rs;


import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ditemis.logic.TestNodeManager;

@Path("/test")
public class TestService {
    
    @Inject
    private TestNodeManager testNodeMgr;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewTestNode() throws RepositoryException {
        return testNodeMgr.buildTestNode();
    }

    @PUT
    @Path("/{nodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTestNode(@PathParam(value = "nodeId") String nodeId) throws RepositoryException {
        return testNodeMgr.updateTestNode(nodeId, "update");
    }
}
