package tp_continua.client;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 17-11-2013
 * Time: 23:37
 * Student Number: 8090309
 */
public interface QueryDispatcher {

    public void addQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener);
    public void removeQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener);
    public void addQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener);
    public void removeQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener);
    
}
