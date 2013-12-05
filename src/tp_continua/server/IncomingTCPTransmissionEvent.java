package tp_continua.server;

import tp_continua.Peer;

import java.net.Socket;
import java.util.EventListener;


/**
 * Event for when a TCP transmission has been received
 */
public class IncomingTCPTransmissionEvent extends IncomingTransmissionEvent {

    private Socket socket;

    /**
     * @param source  Peer which open connection
     * @param message Token received within the message
     * @param socket  Open socket available for communication
     */
    public IncomingTCPTransmissionEvent(Peer source, String message, Socket socket) {
        super(source, message, Protocol.TCP);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public interface IncomingTCPTransmissionEventListener extends EventListener {
        void incomingTCPTransmission(IncomingTCPTransmissionEvent event);
    }
}
