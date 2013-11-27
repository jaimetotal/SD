package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 24-09-2013
 * Time: 20:48
 * To change this template use PeerFile | Settings | PeerFile Templates.
 */
public class Server {

    public void startUp() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 7000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }


        String userInput;

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String prefix = serverSocket.getLocalSocketAddress() + ":" + clientSocket.getLocalAddress() + ":";

        while ((userInput = in.readLine()) != null && !userInput.equals("exit")) {
            System.out.println("Input received: " + userInput);
            out.println(prefix + userInput);
        }

        System.out.println("Cliente fugiu!");
    }

}
