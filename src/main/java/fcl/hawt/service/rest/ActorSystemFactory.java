/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.service.rest;

import akka.actor.ActorSystem;
import akka.actor.Props;
import fcl.hawt.actors.WorkflowDirectory;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;

/**
 *
 * @author rfk
 */
@Singleton
public class ActorSystemFactory implements Factory<ActorSystem>{

    private final ActorSystem system;
    
    public ActorSystemFactory() {
        this.system = ActorSystem.create("hawtSystem");
        system.actorOf(Props.create(WorkflowDirectory.class), "workflowDirectory");
        System.out.println("Registered Workflow Directory: /user/workflowDirectory");
    }
    
    public ActorSystem system() {
        return system;
    }

    @Override
    public ActorSystem provide() {
        return system;
    }

    @Override
    public void dispose(ActorSystem instance) {
    }
    
}
