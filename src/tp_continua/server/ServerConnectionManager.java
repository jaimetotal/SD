package tp_continua.server;

import tp_continua.common.ConnectionManager;
import tp_continua.common.InternalLogger;
import tp_continua.common.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Server-side connection manager, with methods specific for this scenario
 */
public class ServerConnectionManager extends ConnectionManager {

    private final InternalLogger logger;
    private boolean shutdown;
    private ExecutorService executorService;
    private Server server;


    /**
     * Opens sockets for incoming TCP and UDP based on ConnectionManager settings
     *
     * @param server          Server to fire events
     * @param executorService Pool to add TCP/UDP transmission runnable
     */
    public ServerConnectionManager(Server server, ExecutorService executorService) throws IOException {
        logger = InternalLogger.getLogger(this.getClass());
        this.server = server;
        this.executorService = executorService;
        this.executorService.submit(new IncomingTCP());
        this.executorService.submit(new IncomingUDP());
    }

    /**
     * Opens a TCP connection and for each incoming TCP, fires a event with the message received and the sender
     */
    private class IncomingTCP implements Runnable {

        private ServerSocket socketServer;

        public IncomingTCP() throws IOException {
            try {
                logger.info("Starting incoming TCP listener.");
                logger.info("Opening socket %d.", ConnectionManager.SERVER_TCP_PORT);
                socketServer = new ServerSocket(ConnectionManager.SERVER_TCP_PORT);
            } catch (IOException e) {
                logger.error(e, "Error while opening TCP port %d", ConnectionManager.SERVER_TCP_PORT);
                throw e;
            }
        }


        @Override
        public void run() {
            do {
                try {
                    //Waits for a client to connect and fires the event. The event will be responsible to close socket
                    Socket incomingSocket = socketServer.accept();
                    logger.info("Accepted a new connection");
                    Peer client = new Peer(incomingSocket.getInetAddress(), incomingSocket.getPort());
                    logger.info("Incoming TCP from %s.", client);
                    InputStreamReader input = new InputStreamReader(incomingSocket.getInputStream());
                    BufferedReader stdIn = new BufferedReader(input);
                    String message = stdIn.readLine();

                    logger.info("Firing IncomingTCPTransmissionEvent.");
                    IncomingTCPTransmissionEvent event = new IncomingTCPTransmissionEvent(client, message, incomingSocket, stdIn);
                    server.incomingTCPTransmission(event);
                } catch (IOException e) {
                    logger.error(e, "Error while waiting for incoming TCP.");
                }
            } while (!shutdown);
            try {
                socketServer.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Opens a UDP connection and for each incoming UDP, fires a event with the message received and the sender
     */
    private class IncomingUDP implements Runnable {
        @Override
        public void run() {
            logger.info("Starting incoming UDP listener.");
            while (!shutdown) {
                logger.info("Starting multicast listener");
                try (MulticastSocket socket = getMulticastListenerSocket()) {
                    //TODO Test what happens incoming when passes 256 bytes
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    //waits for input
                    logger.info("Standing by for multicast.");
                    socket.receive(packet);
                    //parses input
                    String received = new String(packet.getData(), 0, packet.getLength());
                    //fires incoming transmission event
                    Peer client = new Peer(packet.getAddress(), packet.getPort());
                    logger.info("Receiving message %s from %s.", received, client);
                    IncomingUDPTransmissionEvent event = new IncomingUDPTransmissionEvent(client, received);
                    server.incomingUDPTransmission(event);
                } catch (IOException e) {
                    logger.error(e, "Error while waiting for incoming UDP.");
                }
            }
        }
    }

    public void shutDown() {
        this.shutdown = true;
    }
}
