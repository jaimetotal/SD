package tp_continua.server;

import tp_continua.Peer;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 21:30
 * Student Number: 8090309
 */
public class IncomingUDPTransmissionEvent extends IncomingTransmissionEvent {

    public IncomingUDPTransmissionEvent(Peer source, String message) {
        super(source, message, Protocol.UDP);
    }

    public interface IncomingUDPTransmissionEventListener extends EventListener {
        void incomingUDPTransmission(IncomingUDPTransmissionEvent event);
    }
}
