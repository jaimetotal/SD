package tp_continua.client;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:57
 * Student Number: 8090309
 */
public interface DownloadManagerDispatcher {

    public void addDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener);
    public void removeDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener);
    public void addDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener);
    public void removeDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener);

}
