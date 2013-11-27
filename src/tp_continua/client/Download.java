package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.PeerFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class Download implements Runnable {

    private PeerFile peerFile;
    private Client client;
    private ClientConnectionManager connectionManager;

    public Download(ClientConnectionManager connectionManager, PeerFile peerFile, Client client) {
        this.client = client;
        this.peerFile = peerFile;
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        String command = String.format("%s\n%s", ConnectionManager.DOWNLOAD_FILE, peerFile.getFileName());
        try (ByteArrayOutputStream stream = connectionManager.getOutputStream(peerFile.getNode(), command)) {
            if (stream.size() > 0) {
                peerFile.setContents(stream.toByteArray());
                client.downloadCompleted(new DownloadCompletedEvent(peerFile));
            } else {
                client.downloadFailed(new DownloadFailedEvent(peerFile));
            }

        } catch (IOException e) {
            e.printStackTrace();
//TODO Treat exception
        }
    }
}
