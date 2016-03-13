/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.service.rest;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import fcl.hawt.actors.StatefulActor;
import fcl.hawt.actors.WorkflowDirectory;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import scala.concurrent.Await;
import scala.concurrent.Awaitable;

/**
 *
 * @author rfk
 */
@Path("/register")
public class RegisterWorkflowResource {

    private final ActorSystem system;
    
    @Inject
    public RegisterWorkflowResource(ActorSystem system) {
        this.system = system;
        System.out.println("GOT ACTOR SYSTEM");
    }
    
    @GET
    @Path("/{workflowName}")
    public Response registerWorkflow(@PathParam("workflowName") String workflowName) throws Exception {
        Timeout t = Timeout.apply(5,TimeUnit.SECONDS);
        Props workflowSpec = Props.create(StatefulActor.class, workflowName);
        ActorSelection workflowDirectory = system.actorSelection("/user/workflowDirectory");
        ActorRef workflowRef = (ActorRef) Await.result(Patterns.ask(workflowDirectory, new WorkflowDirectory.WorkflowDef(workflowName, workflowSpec), t),t.duration());
        return Response.ok("Added workflow : " + workflowRef.path()).build();
    }
    
    @GET
    public Response getWorkflows() throws Exception {
        Timeout t=  Timeout.apply(5, TimeUnit.SECONDS);
        ActorSelection workflowDirectory = system.actorSelection("/user/workflowDirectory");
        Map<String,Props> workflows = (Map<String,Props>)Await.result(Patterns.ask(workflowDirectory, new WorkflowDirectory.GetWorkflows(), t), t.duration());
        String result = workflows.entrySet().stream().map(Entry::getKey).collect(Collectors.joining(", "));
        return Response.ok(result).build();
    }
    
}
