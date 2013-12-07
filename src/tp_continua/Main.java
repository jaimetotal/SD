package tp_continua;

import tp_continua.client.Client;
import tp_continua.client.FileAlreadyDownloadingException;
import tp_continua.server.Server;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 03-12-2013
 * Time: 19:47
 * Student Number: 8090309
 */
public class Main {

    private static Client client;
    private static Server server;
    private static FileSystem fs;

    public static void main(String[] args) {
        fs = new FileSystem(".\\filesamples");
        client = new Client(fs);
        client.start();
        server = new Server(fs);
        server.start();
        boolean exit = false;
        do {
//            try {
            queryFiles();
            //System.in.read();
            queryFiles();
/*            } catch (IOException e) {

            }*/
        } while (!exit);
    }

    private static void queryFiles() {
        client.queryNetwork();
    }

    private static void downloadFile() {
        try {
            client.getFile((PeerFile) fs.listFiles().toArray()[0]);
        } catch (FileAlreadyDownloadingException e) {
            System.out.println("Já querias n?");
        }
    }

}
