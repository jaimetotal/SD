package tp_continua.server;

import tp_continua.common.Peer;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.EventListener;


/**
 * Event for when a TCP transmission has been received
 */
public class IncomingTCPTransmissionEvent extends IncomingTransmissionEvent {

    private Socket socket;
    private BufferedReader stdIn;

    /**
     * @param source  Peer which open connection
     * @param message Token received within the message
     * @param socket  Open socket available for communication
     * @param stdIn   BufferedReader used to read the socket's incoming message. Required because another reader can't be used on the same stream and thus it is no longer possible to continue to parse the inputstream.
     */
    public IncomingTCPTransmissionEvent(Peer source, String message, Socket socket, BufferedReader stdIn) {
        super(source, message, Protocol.TCP);
        this.socket = socket;
        this.stdIn = stdIn;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getStdIn() {
        return stdIn;
    }

    public interface IncomingTCPTransmissionEventListener extends EventListener {
        void incomingTCPTransmission(IncomingTCPTransmissionEvent event);
    }
}
