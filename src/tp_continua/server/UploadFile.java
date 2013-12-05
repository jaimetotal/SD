package tp_continua.server;

import tp_continua.FileSystem;
import tp_continua.PeerFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 */
public class UploadFile implements Runnable {

    private Socket socket;
    private FileSystem fileSystem;

    public UploadFile(Socket socket, FileSystem fileSystem) {
        this.socket = socket;
        this.fileSystem = fileSystem;
    }

    @Override
    public void run() {
        try {
            BufferedReader stdIn = new BufferedReader(new BufferedReader(null));
            String fileName = stdIn.readLine();
            PeerFile peerFile = fileSystem.getFileByName(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.write(peerFile.getContents());
            outputStream.close();
            socket.close();
            stdIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO Treat exception
        }
    }
}
