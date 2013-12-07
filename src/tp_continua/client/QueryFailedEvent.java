package tp_continua.client;

import tp_continua.Peer;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Event for when a query process or a index parsing has failed
 */
public class QueryFailedEvent extends EventObject {

    /**
     * Peer which failed to receive index, null if the query process has failed instead
     *
     * @param source
     */
    public QueryFailedEvent(Peer source) {
        super(source);
    }

    public QueryFailedEvent() {
        super("Global");
    }


    @Override
    public Peer getSource() {
        if (super.getSource().equals("Global")) return null;
        return (Peer) super.getSource();
    }

    public interface QueryFailedEventListener extends EventListener {
        void queryFailed(QueryFailedEvent e);
    }
}
