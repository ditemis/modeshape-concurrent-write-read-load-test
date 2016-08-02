package com.ditemis.rs;


import com.ditemis.logic.TestNodeManager;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
