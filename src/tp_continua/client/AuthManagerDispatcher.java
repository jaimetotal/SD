package tp_continua.client;

/**
 * Collection of listeners required to manage authentication' listeners
 */
public interface AuthManagerDispatcher {

    public void addAuthResponseEventListener(AuthResponseEvent.AuthResponseEventListener listener);

    public void removeAuthResponseEventListener(AuthResponseEvent.AuthResponseEventListener listener);

    public void addAuthFailedEventListener(AuthFailedEvent.AuthFailedEventListener listener);

    public void removeAuthFailedEventListener(AuthFailedEvent.AuthFailedEventListener listener);

}
