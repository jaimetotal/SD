package tp_continua.server;

import tp_continua.common.ConnectionManager;
import tp_continua.common.InternalLogger;
import tp_continua.common.Peer;

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
    private InternalLogger logger;

    /**
     * Constructor for Acknowledge
     *
     * @param connectionManager Connection manager to obtain
     * @param source            Peer to reply
     */
    public Acknowledge(ServerConnectionManager connectionManager, Peer source) {
        logger = InternalLogger.getLogger(this.getClass());
        this.connectionManager = connectionManager;
        this.source = source;
    }

    /**
     * Sends a ACK TOKEN with the available port to download
     */
    @Override
    public void run() {
        String message = String.format("%s %s", ConnectionManager.QUERY_FILES_ACK, ConnectionManager.SERVER_TCP_PORT);
        logger.info("Sending ACK to %s in %s format.", source, message);
        byte[] buf = message.getBytes();
        try (DatagramSocket socket = connectionManager.getUDPSocket(false)) {
            InetAddress address = InetAddress.getByName(source.getAddress());
            socket.send(new DatagramPacket(buf, buf.length, address, source.getPort()));
        } catch (IOException e) {
            //Fails reply. In this case, the client won't be aware of this peer
            logger.error(e, "Error while sending ACK.");
        }
    }
}
