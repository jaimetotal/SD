package tp_continua.client;

import tp_continua.common.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * Task for querying files in network
 */
public class QueryFiles implements Runnable {

    private Client client;
    private InternalLogger logger;

    /**
     * Constructor for QueryFiles
     *
     * @param client Client associated to fire events
     */
    public QueryFiles(Client client) {
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
            logger.info("Starting File querying");
            String message = String.format("%s %d", Protocol.QUERY_FILES, ConnectionManager.CLIENT_UDP_PORT);
            logger.info("Sending multicast message: %s ", message);
            ConnectionManager.sendMulticast(message);
            ConnectionManager.listenForUDP(new UDPResponseCallBack() {
                @Override
                public void messageReceived(String messageReceived, Peer peer) {
                    if (messageReceived.startsWith(Protocol.QUERY_FILES_ACK)) {
                        int port = new Scanner(messageReceived.substring(Protocol.QUERY_FILES_ACK.length())).nextInt();
                        logger.info("Port available to fetch files' list: %d", port);
                        peer.setPort(port);
                        new Thread(new FetchList(peer)).start();
                    }
                }
            });
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
    private class FetchList implements Runnable {
        private Peer node;

        private FetchList(Peer node) {
            this.node = node;
        }

        /**
         * Starts streaming the contents from the TCP connection to generate an index.
         */
        @Override
        public void run() {
            logger.info("Parsing files' list");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ConnectionManager.downloadContent(out, node, Protocol.QUERY_FILES);

            try (ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(stream);
                Index index = (Index) objectInputStream.readObject();
                objectInputStream.close();
                logger.info("Parsing completed. Total files: %d", index.getFilesName().size());
                client.queryCompleted(new QueryCompletedEvent(index, node));
            } catch (Exception e) {
                logger.error(e, "Error while trying to parse files' list");
                client.queryFailed(new QueryFailedEvent(node));
            }
        }
    }
}