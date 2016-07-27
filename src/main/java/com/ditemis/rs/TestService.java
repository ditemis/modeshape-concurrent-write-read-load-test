package com.ditemis.rs;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    public String getNewTestNode() {
        return testNodeMgr.buildTestNode();
    }
}
