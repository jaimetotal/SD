package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Small runnable to reply an ACK token for a query request and the available port to use
 */
public class Acknowledge implements Runnable {

    private ServerConnectionManager connectionManager;
    private Peer source;

    /**
     * Constructor for Acknowledge
     *
     * @param connectionManager Connection manager to obtain
     * @param source            Peer to reply
     */
    public Acknowledge(ServerConnectionManager connectionManager, Peer source) {
        this.connectionManager = connectionManager;
        this.source = source;
    }

    /**
     * Sends a ACK TOKEN with the available port to download
     */
    @Override
    public void run() {
        String message = String.format("%s %s", ConnectionManager.QUERY_FILES_ACK, ConnectionManager.SERVER_TCP_PORT);
        byte[] buf = message.getBytes();
        try (DatagramSocket socket = connectionManager.getUDPSocket()) {
            InetAddress address = InetAddress.getByName(source.getAddress());
            socket.send(new DatagramPacket(buf, buf.length, address, ConnectionManager.CLIENT_UDP_PORT));
        } catch (IOException e) {
            //Fails reply. In this case, the client won't be aware of this peer
        }
    }
}
