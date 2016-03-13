/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author rfk
 */
@Path("echo")
public class EchoResource {

    @GET
    @Path("/{payload}")
    public Response echo(@PathParam("payload") String payload) {
        return Response.ok(payload).build();
    }
    
}
