/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcl.hawt.service.rest;

import akka.actor.ActorSystem;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author rfk
 */
@ApplicationPath("/")
public class HAWTService extends ResourceConfig {

    public HAWTService() {
        packages("fcl.hawt.service.rest")
                .register(EchoResource.class)
                .register(RegisterWorkflowResource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bindFactory(ActorSystemFactory.class).to(ActorSystem.class)
                                .proxy(true)
                                .proxyForSameScope(false)
                                .in(Singleton.class);
                    }

                });
    }
}
