package tp_continua.client;

import tp_continua.PeerFile;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:54
 * Student Number: 8090309
 */
public class DownloadCompletedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DownloadCompletedEvent(PeerFile source) {
        super(source);
    }

    @Override
    public PeerFile getSource() {
        return (PeerFile) super.getSource();
    }

    public interface DownloadCompletedEventListener extends EventListener {
        void downloadCompleted(DownloadCompletedEvent e);
    }
}
