package fcl.hawt;

import fcl.hawt.actors.StatefulActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.dispatch.OnSuccess;
import akka.japi.pf.ReceiveBuilder;
import akka.japi.pf.UnitPFBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.concurrent.TimeUnit;
import scala.Function1;
import scala.concurrent.Await;
import scala.util.Try;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Config cfg = ConfigFactory.parseString("akka.actor.warn-about-java-serializer-usage=false");
        ActorSystem s = ActorSystem.create("persistentSystem", cfg);
        ActorRef sa = s.actorOf(Props.create(StatefulActor.class, "workflow-runID1"));
        Timeout t = Timeout.apply(10, TimeUnit.SECONDS);        
        Object result = Await.result(Patterns.ask(sa, Boolean.TRUE, t), t.duration());
        System.out.println(result);
        sa.tell("<ready>", ActorRef.noSender());
        result = Await.result(Patterns.ask(sa, Boolean.TRUE, t), t.duration());
        System.out.println(result);
        Await.result(s.terminate(), t.duration());
    }
}
