package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.PeerFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Task for remote file downloading
 */
public class Download implements Runnable {

    private PeerFile peerFile;
    private Client client;
    private ClientConnectionManager connectionManager;

    /**
     * Constructor for Download
     *
     * @param connectionManager ConnectionManager to obtain connections
     * @param peerFile          File to be download
     * @param client            Associated client to fire download-related events
     */
    public Download(ClientConnectionManager connectionManager, PeerFile peerFile, Client client) {
        this.client = client;
        this.peerFile = peerFile;
        this.connectionManager = connectionManager;
    }

    /**
     * Starts TCP connection by sending to server TOKEN + FILENAME, receiving then the byte stream,
     * firing in the download completed/failed event
     */
    @Override
    public void run() {
        String token = String.format("%s\n%s", ConnectionManager.DOWNLOAD_FILE, peerFile.getFileName());
        try (ByteArrayOutputStream stream = connectionManager.getOutputStream(peerFile.getNode(), token)) {
            if (stream.size() > 0) {
                peerFile.setContents(stream.toByteArray());
                client.downloadCompleted(new DownloadCompletedEvent(peerFile));
                return;
            }
        } catch (IOException e) {
            //Also fires failed event
        }
        client.downloadFailed(new DownloadFailedEvent(peerFile, "Error from server while trying downloading file."));
    }
}
