package tp_continua.client;

import org.apache.commons.io.IOUtils;
import tp_continua.ConnectionManager;
import tp_continua.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class Download implements Runnable {

    private Socket socket;
    private File file;
    private Client client;

    public Download(ConnectionManager connectionManager, File file, Client client) {
        this.client = client;
        this.socket = connectionManager.getSocketToServer(file.getNode());
        this.file = file;
    }

    @Override
    public void run() {
        byte[] fileDownload;
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(ConnectionManager.DOWNLOAD_FILE);
            out.println(file.getFileName());
            fileDownload = fetchContents();

            if (fileDownload != null) {
                file.setContents(fileDownload);
                client.downloadCompleted(new DownloadCompletedEvent(file));
            } else {
                client.downloadFailed(new DownloadFailedEvent(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
            //TODO Treat exception
        }

    }

    private byte[] fetchContents() {
        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = socket.getInputStream();
            IOUtils.copy(in, out);
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return out.toByteArray();  //To change body of created methods use File | Settings | File Templates.
    }
}
