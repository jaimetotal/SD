package tp_continua.client;

import tp_continua.Index;
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
public class QueryCompletedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */

    private Index index;


    public QueryCompletedEvent(Index index, Peer source ) {
        super(source);
        this.index= index;
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();    //To change body of overridden methods use File | Settings | File Templates.
    }


    public Index getIndex()
    {
        return index;
    }

    public interface QueryCompletedEventListener extends EventListener {
        void queryCompleted(QueryCompletedEvent e);
    }
}
