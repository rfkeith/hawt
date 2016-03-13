/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.persistence.UntypedPersistentActor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rfk
 */
public class WorkflowDirectory extends UntypedPersistentActor {
    
    private Map<String,Props> workflows ;

    @Override
    public void onReceiveRecover(Object o) throws Exception {
        if(workflows == null) {
            workflows = new HashMap<>();
        }
        if(o instanceof WorkflowDef) {
            createWorkflow((WorkflowDef)o);
        }
    }

    @Override
    public void onReceiveCommand(Object o) throws Exception {
        if(o instanceof GetWorkflows) {
            sender().tell(workflows, self());
        }else if(o instanceof WorkflowDef){
            persist((WorkflowDef)o, (as)-> {
                ActorRef workflowStart = createWorkflow(as);
                sender().tell(workflowStart, self());
            });
            
        }
    }

    private ActorRef createWorkflow(WorkflowDef as) {
        workflows.put(as.name, as.spec);
        System.out.println("Saved " + as.name);
        ActorRef workflowStart = context().actorOf(as.spec, as.name);
        return workflowStart;
    }

    @Override
    public String persistenceId() {
        return "workflowDirectory";
    }
    
    @Override
    public String journalPluginId() {
        return "akka.persistence.journal.leveldb";
    }

    @Override
    public String snapshotPluginId() {
        return "akka.persistence.snapshot-store.local";
    }
    
    public static class GetWorkflows {
        
    }
    
    public static class WorkflowDef implements Serializable {
        
        private final String name;
        private final Props spec;

        public WorkflowDef(String name, Props spec) {
            this.name = name;
            this.spec = spec;
        }
    }
}
