package tp_continua.server;

import tp_continua.Peer;

import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 21:25
 * Student Number: 8090309
 */
public class IncomingTransmissionEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */

    public enum Protocol
    {
        TCP, UDP
    }

    private String message;
    private Protocol protocol;

    public IncomingTransmissionEvent(Peer source, String message, Protocol protocol) {
        super(source);
        this.message = message;
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
