package tp_continua.server;

import tp_continua.Index;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Task to list available files to remote client
 */
public class ListFiles implements Runnable {

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
    }

    /**
     * Serializes the index of available files to the open socket
     */
    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(index);
        } catch (IOException e) {
            //In this case, the connection will fail and the client won't receive the list
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
