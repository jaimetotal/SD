package tp_continua.client;

import tp_continua.common.Peer;

import java.util.EventListener;
import java.util.EventObject;


public class AuthFailedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AuthFailedEvent(Object source) {
        super(source);
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();
    }

    /**
     * Listener to be notified when a download is completed
     */
    public interface AuthFailedEventListener extends EventListener {
        void authFailed(AuthFailedEvent e);
    }
}
