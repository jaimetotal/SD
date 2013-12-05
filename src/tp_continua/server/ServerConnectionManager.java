package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Server-side connection manager, with methods specific for this scenario
 */
public class ServerConnectionManager extends ConnectionManager {

    private boolean shutdown;
    private ExecutorService executorService;
    private Server server;


    /**
     * Opens sockets for incoming TCP and UDP based on ConnectionManager settings
     *
     * @param server          Server to fire events
     * @param executorService Pool to add TCP/UDP transmission runnable
     */
    public ServerConnectionManager(Server server, ExecutorService executorService) {
        this.server = server;
        this.executorService = executorService;
        this.executorService.submit(new IncomingTCP());
        this.executorService.submit(new IncomingUDP());
    }

    /**
     * Opens a TCP connection and for each incoming TCP, fires a event with the message received and the sender
     */
    private class IncomingTCP implements Runnable {
        @Override
        public void run() {
            do {
                try (ServerSocket socketServer = new ServerSocket(ConnectionManager.SERVER_TCP_PORT)) {

                    //Waits for a client to connect and fires the event. The event will be responsible to close socket
                    Socket incomingSocket = socketServer.accept();

                    if (incomingSocket.getInetAddress().isAnyLocalAddress()) {
                        Peer client = new Peer(incomingSocket.getInetAddress(), incomingSocket.getPort());
                        BufferedReader stdIn = new BufferedReader(new BufferedReader(null));
                        String message = stdIn.readLine();
                        stdIn.close();
                        IncomingTCPTransmissionEvent event = new IncomingTCPTransmissionEvent(client, message, incomingSocket);
                        server.incomingTCPTransmission(event);
                    } else {
                        incomingSocket.close();
                    }
                } catch (IOException e) {
                    //TODO maybe fire event?
                }
            } while (!shutdown);
        }
    }

    /**
     * Opens a UDP connection and for each incoming UDP, fires a event with the message received and the sender
     */
    private class IncomingUDP implements Runnable {
        @Override
        public void run() {
            while (!shutdown) {
                try (MulticastSocket socket = getMulticastListenerSocket()) {
                    //TODO Test what happens incoming when passes 256 bytes
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    //waits for input
                    socket.receive(packet);
                    //parses input
                    String received = new String(packet.getData(), 0, packet.getLength());
                    //fires incoming transmission event
                    Peer client = new Peer(packet.getAddress(), packet.getPort());
                    IncomingUDPTransmissionEvent event = new IncomingUDPTransmissionEvent(client, received);
                    server.incomingUDPTransmission(event);
                } catch (IOException e) {
                    //TODO maybe fire connection shutdown event?
                } finally {
                }
            }
        }
    }

    public void shutDown() {
        this.shutdown = true;
    }
}
