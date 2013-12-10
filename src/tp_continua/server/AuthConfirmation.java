package tp_continua.server;

import tp_continua.common.ConnectionManager;
import tp_continua.common.InternalLogger;
import tp_continua.common.Peer;
import tp_continua.common.Protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Responses to client with an OK or not
 */
public class AuthConfirmation implements Runnable {

    private final InternalLogger logger;
    private Peer source;
    private boolean isOK;

    public AuthConfirmation(boolean isValid, Peer source) {
        this.source = source;
        this.logger = InternalLogger.getLogger(this.getClass());
        this.isOK = isValid;
    }

    @Override

    public void run() {
        String message = String.format("%s %s", Protocol.AUTH_RESPONSE, isOK ? "OK" : "NOK");
        logger.info("Sending AUTH_RESPONSE to %s in %s format.", source, message);
        byte[] buf = message.getBytes();
        try (DatagramSocket socket = ConnectionManager.getUDPSocket(false)) {
            InetAddress address = InetAddress.getByName(source.getAddress());
            socket.send(new DatagramPacket(buf, buf.length, address, source.getPort()));
        } catch (IOException e) {
            //Fails reply. In this case, the client won't be aware of this peer
            logger.error(e, "Error while sending AUTH_RESPONSE.");
        }
    }
}
