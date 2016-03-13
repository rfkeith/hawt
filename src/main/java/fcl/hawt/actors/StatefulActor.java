/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.actors;

import akka.persistence.RecoveryCompleted;
import akka.persistence.UntypedPersistentActor;

/**
 *
 * @author rfk
 */
public class StatefulActor extends UntypedPersistentActor{

    private final String name;
    
    private String state = "<init>";
    
    public StatefulActor(String name) {
        this.name = name;
    }

    @Override
    public void onReceiveRecover(Object o) throws Exception {
        if(!(o instanceof RecoveryCompleted)) {
            state = o.toString();
        }
    }

    @Override
    public void onReceiveCommand(Object o) throws Exception {
        if(o instanceof String) {
            persist(o, (str)-> {
                state = str.toString();
            });
        } else if (o instanceof Boolean) {
            sender().tell(state, self());
        }
    }

    @Override
    public String persistenceId() {
        return name;
    }

    @Override
    public String journalPluginId() {
        return "akka.persistence.journal.leveldb";
    }

    @Override
    public String snapshotPluginId() {
        return "akka.persistence.snapshot-store.local";
    }
    
    
    
}
