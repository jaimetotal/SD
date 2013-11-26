package tp_continua.server;

import tp_continua.File;
import tp_continua.FileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 22:00
 * Student Number: 8090309
 */
public class UploadFile implements  Runnable {

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
            File file = fileSystem.getFileByName(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.write(file.getContents());
            outputStream.close();
            socket.close();
            stdIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO Treat exception
        }
    }
}
