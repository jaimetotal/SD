package tp_continua.server;

import tp_continua.Index;
import tp_continua.InternalLogger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Task to list available files to remote client
 */
public class ListFiles implements Runnable {

    private final InternalLogger logger;
    private Socket socket;
    private Index index;

    /**
     * Constructor for ListFiles
     *
     * @param socket Open socket for communication
     * @param index  Index of files available for download
     */
    public ListFiles(Socket socket, Index index) {
        this.socket = socket;
        this.index = index;
        logger = InternalLogger.getLogger(this.getClass());
    }

    /**
     * Serializes the index of available files to the open socket
     */
    @Override
    public void run() {
        logger.info("Starting to list files for incoming request.");
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(index);
            objectOutput.close();
        } catch (IOException e) {
            logger.error(e, "Error while trying to send index.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
