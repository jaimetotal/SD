package tp_continua.client;

/**
 * Collection of listeners required to manage downloads' clients
 */
public interface DownloadManagerDispatcher {

    public void addDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener);

    public void removeDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener);

    public void addDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener);

    public void removeDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener);

}
