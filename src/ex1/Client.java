package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 24-09-2013
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private Socket echoSocket;
    private BufferedReader stdIn;

    public void connect(String machineName) throws IOException {
        echoSocket = new Socket(machineName, 7000);
        out = new PrintWriter(echoSocket.getOutputStream(), true);
        in = new BufferedReader( new InputStreamReader(echoSocket.getInputStream()));
        stdIn =  new BufferedReader( new InputStreamReader(System.in));
    }

    public void connect() throws IOException {
        connect("localhost");
    }

    public void read() throws IOException {
        String userInput;
        while ((userInput = stdIn.readLine()) != null && !userInput.equals("exit")) {
            out.println(userInput);
            System.out.println("response from server: " + in.readLine());
            if(userInput.equals("exit"))
            {
                break;
            }
        }
        System.out.println("Client saiu");
    }

    public void close() throws IOException {
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }

}
