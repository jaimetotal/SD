package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.File;

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

    private File file;
    private Client client;
    private ClientConnectionManager connectionManager;

    public Download(ClientConnectionManager connectionManager, File file, Client client) {
        this.client = client;
        this.file = file;
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        String command = String.format("%s\n%s", ConnectionManager.DOWNLOAD_FILE, file.getFileName());
        try (ByteArrayOutputStream stream = connectionManager.getOutputStream(file.getNode(), command)) {
            if (stream.size() > 0) {
                file.setContents(stream.toByteArray());
                client.downloadCompleted(new DownloadCompletedEvent(file));
            } else {
                client.downloadFailed(new DownloadFailedEvent(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
//TODO Treat exception
        }
    }
}
