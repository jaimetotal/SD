package tp_continua.app;

import tp_continua.client.*;
import tp_continua.common.FileSystem;
import tp_continua.common.PeerFile;
import tp_continua.server.Server;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

public class Main implements DownloadCompletedEvent.DownloadCompletedEventListener, DownloadFailedEvent.DownloadFailedEventListener, QueryCompletedEvent.QueryCompletedEventListener, QueryFailedEvent.QueryFailedEventListener {

    private Client client;
    private Server server;
    private FileSystem fs;

    public Main() throws IOException {
        fs = new FileSystem(".\\filesamples");
        server = new Server(fs);
        server.start();
        client = new Client(fs);
        client.start();
        client.addDownloadCompletedEventListener(this);
        client.addDownloadFailedEventListener(this);
        client.addQueryCompletedEventListener(this);
        client.addQueryFailedEventListener(this);
        client.queryNetwork();
        displayMenu();
    }

    public static void main(String[] args) throws IOException {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        System.out.println("Dir: " + System.getProperty("user.dir"));

        for (String entry : classpathEntries) {
            System.out.println("Classpath " + entry);
        }
        new Main();
    }

    /**
     * Displays menu
     */
    private void displayMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("1) Consult Files \n2) Download File \n3) Check downloads in progress\n4)Refresh available files\ns) Exit");
            System.out.print("Selection: ");
            Scanner in = new Scanner(System.in);
            String next;
            try {
                next = in.next();
            } catch (java.util.NoSuchElementException ex) {
                next = "s";
            }

            switch (next) {
                case "1":
                    listFiles();
                    break;
                case "2":
                    downloadFiles();
                    break;
                case "3":
                    checkDownloadProgress();
                    break;
                case "4":
                    queryNetwork();
                    break;
                case "s":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void queryNetwork() {
        System.out.println("Querying network...");
        client.queryNetwork();
    }

    private void checkDownloadProgress() {
        Enumeration<PeerFile> enumerator = client.getDownloadingFiles();
        while (enumerator.hasMoreElements()) {
            PeerFile file = enumerator.nextElement();
            System.out.println(String.format("File %s.", file));
        }
    }

    private void listFiles() {
        if (fs.getRemoteFiles().isEmpty()) {
            System.out.println("There isn't any available to download.");
            return;
        }
        int n = 1;
        for (PeerFile f : fs.getRemoteFiles()) {
            System.out.println(String.format("%d) %s", n, f.getFileName()));
            n++;
        }
    }


    private void downloadFiles() {

        listFiles();
        System.out.println("The files on network:\na) Download all");
        System.out.println("s) Return");
        Scanner in = new Scanner(System.in);
        String next = in.next();
        switch (next) {
            case "a":
                for (PeerFile file : fs.getRemoteFiles()) {
                    try {
                        client.getFile(file);
                    } catch (FileAlreadyDownloadingException e) {
                        System.out.println(String.format("SYSTEM: File %s is already downloading...", file));
                    }
                }
                break;

            case "s":
                break;
            default:
                Integer value = Integer.getInteger(next);

                if (value == null || value <= 0 || fs.getRemoteFiles().size() < value) {
                    System.out.println("Invalid option.");
                } else {
                    try {
                        client.getFile((PeerFile) fs.getRemoteFiles().toArray()[value - 1]);
                    } catch (FileAlreadyDownloadingException e) {
                        System.out.println("SYSTEM: File is already downloading...");
                    }
                }
                break;
        }

    }

    @Override
    public void downloadCompleted(DownloadCompletedEvent e) {
        System.out.println(String.format("SYSTEM: Download completed for %s.", e.getSource()));
    }

    @Override
    public void downloadFailed(DownloadFailedEvent e) {
        System.out.println(String.format("SYSTEM: Download failed for %s.", e.getSource()));
    }

    @Override
    public void queryCompleted(QueryCompletedEvent e) {
        System.out.println("SYSTEM: File querying finished.");
    }

    @Override
    public void queryFailed(QueryFailedEvent e) {
        if (e.getSource() == null) {
            System.out.println("SYSTEM: Failed to obtain files to download. Try again later.");
        }
    }
}
