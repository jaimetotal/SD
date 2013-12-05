package tp_continua.client;

import tp_continua.Index;
import tp_continua.Peer;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Event for when a query from a peer is successfully received
 */
public class QueryCompletedEvent extends EventObject {

    private Index index;

    /**
     * @param index  Collection of files received
     * @param source Peer which reported index
     */
    public QueryCompletedEvent(Index index, Peer source) {
        super(source);
        this.index = index;
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();    //To change body of overridden methods use PeerFile | Settings | PeerFile Templates.
    }


    public Index getIndex() {
        return index;
    }

    public interface QueryCompletedEventListener extends EventListener {
        void queryCompleted(QueryCompletedEvent e);
    }
}
