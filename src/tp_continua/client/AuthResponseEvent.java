package tp_continua.client;

import tp_continua.common.AuthResponse;
import tp_continua.common.Peer;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Event for when an auth response arrives
 */
public class AuthResponseEvent extends EventObject {

    private final AuthResponse response;

    public AuthResponseEvent(Peer source, AuthResponse response) {
        super(source);
        this.response = response;
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();
    }

    public AuthResponse getResponse() {
        return response;
    }

    /**
     * Listener to be notified when a auth response is received
     */
    public interface AuthResponseEventListener extends EventListener {
        void authResponse(AuthResponseEvent e);
    }
}
