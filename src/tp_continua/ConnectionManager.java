package tp_continua;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class ConnectionManager {

    //http://stackoverflow.com/questions/2821658/java-sockets-multiple-client-threads-on-same-port-on-same-machine

    private List<Integer> portPool;

    public static final String QUERY_PORT = "QUERY_PORT";
    public static final String QUERY_FILES = "QUERY_FILES";
    public static final String DOWNLOAD_FILE = "DOWNLOAD_FILE";
    public static int SERVER_PORT = 7123;

    public ConnectionManager()
    {
        portPool = new ArrayList<Integer>();
        String port = System.getProperty("port.listen");
    }

    public Socket getSocketToServer(Peer node) {
        try {
            //TODO Query port for stream
            Socket socket = new Socket(node.getAddress().toString(), node.getPort());
            return socket;
        } catch (IOException e) {

        }
        return null;
    }




}
