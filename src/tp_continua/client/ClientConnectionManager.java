package tp_continua.client;

import org.apache.commons.io.IOUtils;
import tp_continua.ConnectionManager;
import tp_continua.Peer;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 22:25
 * Student Number: 8090309
 */
public class ClientConnectionManager extends ConnectionManager {

    public ByteArrayInputStream getInputStream(Peer node, String command)
    {
        return new ByteArrayInputStream(getOutputStream(node, command).toByteArray());
    }

    public ByteArrayOutputStream getOutputStream(Peer node, String command)
    {

        InputStream in;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(Socket socket = getTCPSocketToPeer(node)) {


            PrintWriter streamWriter = new PrintWriter(socket.getOutputStream(), true);
            streamWriter.println(command);

            //TODO send command and receive
            in = getTCPSocketToPeer(node).getInputStream();
            IOUtils.copy(in, out);
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
        }
        return out;
    }
}
