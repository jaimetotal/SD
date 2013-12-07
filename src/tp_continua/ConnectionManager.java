package tp_continua;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.*;

public class ConnectionManager {

    public static final String QUERY_FILES = "QUERY_FILES";
    public static final String QUERY_FILES_ACK = "QUERY_FILES_ACK";
    public static final String DOWNLOAD_FILE = "DOWNLOAD_FILE";
    public static int SERVER_TCP_PORT = 7123;
    public static int CLIENT_UDP_PORT = 7712;
    public static int MULTICAST_TIMEOUT = 1000;
    public static String MULTICAST_ADDRESS = "230.0.0.1";
    public static int MULTICAST_PORT = 4456;
    private static final InternalLogger logger = InternalLogger.getLogger(ConnectionManager.class);

    public ConnectionManager() {
        //TODO Fetch properties
    }


    public static Socket getTCPSocketToPeer(Peer node) {
        try {
            logger.info("Opening socket for %s:%d", node.getAddress().toString(), ConnectionManager.SERVER_TCP_PORT);
            return new Socket(node.getAddress().toString(), ConnectionManager.SERVER_TCP_PORT);
        } catch (IOException e) {
            logger.error(e, "Error while trying to open TCP Socket.");
        }
        return null;
    }

    public static DatagramSocket getUDPSocket(boolean setPort) {
        DatagramSocket responseSocket = null;
        try {
            if (setPort) {
                responseSocket = new DatagramSocket(ConnectionManager.CLIENT_UDP_PORT);
                logger.info("Obtained UDP socket open in port: %d", ConnectionManager.CLIENT_UDP_PORT);
            } else {
                responseSocket = new DatagramSocket();
                logger.info("Obtained UDP socket open in port: %d", responseSocket.getPort());
            }

            responseSocket.setSoTimeout(ConnectionManager.MULTICAST_TIMEOUT);
        } catch (SocketException e) {
            logger.error(e, "Error while opening socket.");
        }
        return responseSocket;
    }

    public static void sendMulticast(String message) {
        InetAddress group;
        byte[] buf = message.getBytes();
        try (DatagramSocket socket = new DatagramSocket()) {
            group = InetAddress.getByName(ConnectionManager.MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, ConnectionManager.MULTICAST_PORT);
            socket.send(packet);
        } catch (UnknownHostException e) {
            logger.error(e, "Trying to send multicast message.");
        } catch (SocketException e) {
            logger.error(e, "Trying to send multicast message.");
        } catch (IOException e) {
            logger.error(e, "Trying to send multicast message.");
        }
    }

    public ByteArrayInputStream getInputStream(Peer node, String command) {
        return new ByteArrayInputStream(getOutputStream(node, command).toByteArray());
    }

    public static void uploadContent(InputStream inputStream, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        IOUtils.copy(inputStream, out);
        out.flush();
        out.close();
        inputStream.close();
    }

    public static void downloadContent(OutputStream outputStream, Peer node, String token) {
        InputStream in;
        try (Socket socket = getTCPSocketToPeer(node)) {
            //Sends token
            logger.info("Sending message to target %s:", token);
            PrintWriter streamWriter = new PrintWriter(socket.getOutputStream(), true);
            streamWriter.println(token);
            logger.info("Now waiting for input.");
            //Receives content
            in = socket.getInputStream();
            IOUtils.copy(in, outputStream);
            logger.info("Input received.");
            outputStream.flush();
            socket.close();
            outputStream.close();
            in.close();
        } catch (IOException e) {
            logger.error(e, "Error while trying to obtain output stream from socket.");
        }
    }

    public static ByteArrayOutputStream getOutputStream(Peer node, String token) {

        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Socket socket = getTCPSocketToPeer(node)) {
            //Sends token
            logger.info("Sending message to target %s:", token);
            PrintWriter streamWriter = new PrintWriter(socket.getOutputStream(), true);
            streamWriter.println(token);
            logger.info("Now waiting for input.");
            //Receives content
            in = socket.getInputStream();
            IOUtils.copy(in, out);
            logger.info("Input received.");
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            logger.error(e, "Error while trying to obtain output stream from socket.");
        }
        return out;
    }

    public static MulticastSocket getMulticastListenerSocket() throws IOException {
        MulticastSocket socket = new MulticastSocket(ConnectionManager.MULTICAST_PORT);
        InetAddress address = InetAddress.getByName(ConnectionManager.MULTICAST_ADDRESS);
        socket.joinGroup(address);
        return socket;
    }

}
