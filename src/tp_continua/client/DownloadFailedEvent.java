package tp_continua.client;

import tp_continua.File;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:55
 * Student Number: 8090309
 */
public class DownloadFailedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */
    public DownloadFailedEvent(File source) {
        super(source);
    }

    @Override
    public File getSource() {
        return (File) super.getSource();
    }

    public interface DownloadFailedEventListener extends EventListener{
        void downloadFailed(DownloadFailedEvent e);
    }
}
