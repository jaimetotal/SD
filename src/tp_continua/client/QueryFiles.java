package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.Index;
import tp_continua.InternalLogger;
import tp_continua.Peer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * Task for querying files in network
 */
public class QueryFiles implements Runnable {

    private ClientConnectionManager connectionManager;
    private Client client;
    private InternalLogger logger;

    /**
     * Constructor for QueryFiles
     *
     * @param connectionManager Connection manager to obtain connections to server
     * @param client            Client associated to fire events
     */
    public QueryFiles(ClientConnectionManager connectionManager, Client client) {
        this.connectionManager = connectionManager;
        this.client = client;
        logger = InternalLogger.getLogger(this.getClass());
    }

    /**
     * Starts UDP connection by sending to multicast network a QUERY_FILES TOKEN, receiving then from each available peer
     * an QUERY_FILES_ACK token until reaches the connection timeout. For each peer, a new runnable is executed to parse the peer's available files.
     * In case the network query has failed or the parsing of a peer's files fails, a query failed event is fired.
     * For each successful peer's files parsing, a query completed event is fired.
     */
    @Override
    public void run() {
        DatagramSocket responseSocket = null;
        try {
            logger.warn("Starting File querying");
            responseSocket = connectionManager.getUDPSocket(true);
            logger.warn("Sending multicast message: %s ", ConnectionManager.QUERY_FILES);
            connectionManager.sendMulticast(ConnectionManager.QUERY_FILES);
            //Receives ConnectionManager.QUERY_FILES_ACK + PORT SIZE + \n
            byte[] responseBuf = new byte[ConnectionManager.QUERY_FILES_ACK.length() + 5];
            DatagramPacket packet = new DatagramPacket(responseBuf, responseBuf.length);

            boolean timeOut = false;
            while (!timeOut) {
                try {
                    responseSocket.receive(packet);
                    String messageReceived = packet.getData().toString();
                    logger.info("Message received from %s:%s: %s.", packet.getAddress(), packet.getPort(), messageReceived);
                    if (messageReceived.startsWith(ConnectionManager.QUERY_FILES_ACK)) {
                        int port = new Scanner(messageReceived).nextInt();
                        logger.info("Port available to fetch files' list: " + port);
                        new Thread(new FetchQuery(new Peer(packet.getAddress(), port)));
                    }
                } catch (java.net.SocketTimeoutException ex) {
                    timeOut = true;
                }
            }
        } catch (Exception e) {
            logger.error(e, "Error while waiting for responses.");
            client.queryFailed(new QueryFailedEvent());
        } finally {
            if (responseSocket != null) {
                responseSocket.close();
            }
        }
    }


    /**
     * Small runnable to parse Peer's files
     */
    private class FetchQuery implements Runnable {
        private Peer node;

        private FetchQuery(Peer node) {
            this.node = node;
        }

        /**
         * Starts streaming the contents from the TCP connection to generate an index.
         */
        @Override
        public void run() {
            logger.info("Parsing files' list");
            try (ByteArrayInputStream stream = connectionManager.getInputStream(node, ConnectionManager.QUERY_FILES)) {
                ObjectInputStream objectInputStream = new ObjectInputStream(stream);
                Index index = (Index) objectInputStream.readObject();
                client.queryCompleted(new QueryCompletedEvent(index, node));
                objectInputStream.close();
            } catch (IOException e) {
                client.queryFailed(new QueryFailedEvent(node));
            } catch (ClassNotFoundException e) {
                client.queryFailed(new QueryFailedEvent(node));
            }
        }
    }
}