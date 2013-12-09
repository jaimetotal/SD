package tp_continua.server;

import tp_continua.common.ConnectionManager;
import tp_continua.common.FileSystem;
import tp_continua.common.InternalLogger;
import tp_continua.common.PeerFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 *
 */
public class UploadFile implements Runnable {

    private final InternalLogger logger;
    private Socket socket;
    private FileSystem fileSystem;
    private BufferedReader stdIn;

    public UploadFile(Socket socket, FileSystem fileSystem, BufferedReader stdIn) {
        this.socket = socket;
        this.fileSystem = fileSystem;
        this.stdIn = stdIn;
        logger = InternalLogger.getLogger(this.getClass());
    }


    @Override
    public void run() {
        logger.info("Starting to uploading file");
        try {
            logger.info("Trying to read file name");
            String fileName = stdIn.readLine();
            PeerFile peerFile = fileSystem.getFileByName(fileName);
            logger.info("File asked to be download: %s", peerFile);
            ConnectionManager.uploadContent(peerFile.getContentsStream(), socket);
            logger.info("Transmission completed.", peerFile);
        } catch (IOException e) {
            logger.error(e, "Error while trying to send file.");
        }
    }
}
