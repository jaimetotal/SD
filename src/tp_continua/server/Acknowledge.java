package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 26-11-2013
 * Time: 22:28
 * Student Number: 8090309
 */
public class Acknowledge implements Runnable {

    private ServerConnectionManager connectionManager;
    private Peer source;

    public Acknowledge(ServerConnectionManager connectionManager, Peer source) {
        this.connectionManager = connectionManager;
        this.source = source;
    }

    @Override
    public void run() {
        String message = String.format ("%s %s", ConnectionManager.QUERY_FILES_ACK , ConnectionManager.SERVER_TCP_PORT);
        byte[] buf = message.getBytes();
        try (DatagramSocket socket = connectionManager.getUDPSocket()) {
            socket.send(new DatagramPacket(buf, buf.length, source.getAddress(), ConnectionManager.CLIENT_UDP_PORT));
        } catch (IOException e) {
            //TODO
        }
    }
}
