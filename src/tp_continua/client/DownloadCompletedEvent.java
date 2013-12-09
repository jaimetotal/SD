package tp_continua.client;

import tp_continua.common.PeerFile;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Event to notify when download is completed
 */
public class DownloadCompletedEvent extends EventObject {

    /**
     * @param source File which has been completed
     */
    public DownloadCompletedEvent(PeerFile source) {
        super(source);
    }

    @Override
    public PeerFile getSource() {
        return (PeerFile) super.getSource();
    }

    /**
     * Listener to be notified when a download is completed
     */
    public interface DownloadCompletedEventListener extends EventListener {
        void downloadCompleted(DownloadCompletedEvent e);
    }
}
