package ex1;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 24-09-2013
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting mode");
        System.out.println(" 1 - Client");
        System.out.println(" 2 - Server");
        if(System.in.read() == 50) //2
        {
            startServer();
        }
        else
        {
            startClient();
        }

    }

    private static void startClient()
    {
        Client client = new Client();
        try {
            client.connect();
            client.read();
            client.close();
        } catch (IOException e) {

        }
    }

    private static void startServer() throws IOException {
        Server server = new Server();
        server.startUp();
    }


}
