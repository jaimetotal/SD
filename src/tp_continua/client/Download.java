package tp_continua.client;

import tp_continua.common.ConnectionManager;
import tp_continua.common.InternalLogger;
import tp_continua.common.PeerFile;

import java.io.IOException;

/**
 * Task for remote file downloading
 */
public class Download implements Runnable {

    private final InternalLogger logger;
    private PeerFile peerFile;
    private Client client;

    /**
     * Constructor for Download
     *
     * @param peerFile File to be download
     * @param client   Associated client to fire download-related events
     */
    public Download(PeerFile peerFile, Client client) {
        this.client = client;
        this.peerFile = peerFile;
        logger = InternalLogger.getLogger(this.getClass());
    }

    /**
     * Starts TCP connection by sending to server TOKEN + FILENAME, receiving then the byte stream,
     * firing in the download completed/failed event
     */
    @Override
    public void run() {
        logger.info("Trying to download %s from %s.", peerFile, peerFile.getNode());
        String token = String.format("%s\n%s", ConnectionManager.DOWNLOAD_FILE, peerFile.getFileName());

        try {
            ConnectionManager.downloadContent(peerFile.getDestinationStream(), peerFile.getNode(), token);
            client.downloadCompleted(new DownloadCompletedEvent(peerFile));
        } catch (IOException e) {
            logger.error(e, "Error while downloading.");
            client.downloadFailed(new DownloadFailedEvent(peerFile, "Error from server while trying downloading file."));
        }

    }
}
