package tp_continua.client;

/**
 * Collection of listeners required to manage queries' clients
 */
public interface QueryDispatcher {

    public void addQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener);

    public void removeQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener);

    public void addQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener);

    public void removeQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener);

}
