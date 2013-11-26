package tp_continua.server;

import tp_continua.Peer;

import java.net.Socket;
import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 21:31
 * Student Number: 8090309
 */
public class IncomingTCPTransmissionEvent extends IncomingTransmissionEvent {

    private Socket socket;

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
