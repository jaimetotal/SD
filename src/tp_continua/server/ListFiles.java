package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.Index;
import tp_continua.Peer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 17-11-2013
 * Time: 23:25
 * Student Number: 8090309
 */
public class ListFiles implements Runnable {

    private Socket socket;
    private Index index;

    public ListFiles(ConnectionManager connectionManager, Index index, Peer node) {
        this.socket = connectionManager.getTCPSocketToPeer(node);
        this.index = index;
    }

    @Override
    public void run() {
        try
        {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(index);
        }
        catch (IOException e)
        {
            //TODO Log
            e.printStackTrace();
        }
    }
}
