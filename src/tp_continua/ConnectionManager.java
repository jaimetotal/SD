package tp_continua;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class ConnectionManager {

    public static final String QUERY_FILES = "QUERY_FILES";
    public static final String QUERY_FILES_ACK = "QUERY_FILES_ACK";
    public static final String DOWNLOAD_FILE = "DOWNLOAD_FILE";
    public static int SERVER_TCP_PORT = 7123;
    public static int CLIENT_UDP_PORT = 7712;
    public static int MULTICAST_TIMEOUT = 1000;
    public static String MULTICAST_ADDRESS = "230.0.0.1";
    public static int MULTICAST_PORT = 4456;

    public ConnectionManager() {
        //TODO Fetch properties
    }


    public Socket getTCPSocketToPeer(Peer node) {
        try {
            //TODO Query port for stream
            return new Socket(node.getAddress().toString(), ConnectionManager.SERVER_TCP_PORT);
        } catch (IOException e) {

        }
        return null;
    }

    public DatagramSocket getUDPSocket() {
        //TODO Receive socket from the ConnectionManager
        DatagramSocket responseSocket = null;
        try {
            responseSocket = new DatagramSocket(ConnectionManager.SERVER_TCP_PORT);
            //TODO Revise timeout
            responseSocket.setSoTimeout(ConnectionManager.MULTICAST_TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
//TODO Treat exception
        }
        return responseSocket;
    }

    public void sendMulticast(String message) {
        InetAddress group = null;
        try {
            byte[] buf = message.getBytes();
            DatagramSocket socket = new DatagramSocket(ConnectionManager.CLIENT_UDP_PORT);
            group = InetAddress.getByName(ConnectionManager.MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, ConnectionManager.MULTICAST_PORT);
            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
//TODO Treat exception
        } catch (SocketException e) {
            e.printStackTrace();
//TODO Treat exception
        } catch (IOException e) {
            e.printStackTrace();
//TODO Treat exception
        }

    }

    public ByteArrayInputStream getInputStream(Peer node, String command) {
        return new ByteArrayInputStream(getOutputStream(node, command).toByteArray());
    }

    public ByteArrayOutputStream getOutputStream(Peer node, String token) {

        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Socket socket = getTCPSocketToPeer(node)) {
            //Sends token
            PrintWriter streamWriter = new PrintWriter(socket.getOutputStream(), true);
            streamWriter.println(token);
            //Receives content
            in = socket.getInputStream();
            IOUtils.copy(in, out);
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            //TODO
        }
        return out;
    }

    public MulticastSocket getMulticastListenerSocket() throws IOException {
        MulticastSocket socket = new MulticastSocket(ConnectionManager.MULTICAST_PORT);
        InetAddress address = InetAddress.getByName(ConnectionManager.MULTICAST_ADDRESS);
        socket.joinGroup(address);
        return socket;
    }

}
