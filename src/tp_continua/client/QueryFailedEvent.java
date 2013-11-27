package tp_continua.client;

import tp_continua.Peer;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 17-11-2013
 * Time: 23:38
 * Student Number: 8090309
 */
public class QueryFailedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public QueryFailedEvent(Peer source) {
        super(source);
    }


    @Override
    public Peer getSource() {
        return (Peer) super.getSource();    //To change body of overridden methods use PeerFile | Settings | PeerFile Templates.
    }

    public interface QueryFailedEventListener extends EventListener {
        void queryFailed(QueryFailedEvent e);
    }
}
