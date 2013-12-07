package tp_continua;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class PeerFile {

    private String fileName;
    private byte[] contents;
    private Peer node;
    private boolean isLocal;
    private Path filePath;

    public PeerFile(String fileName) {
        this.filePath = Paths.get(fileName);
        this.fileName = filePath.getFileName().toString();
        this.isLocal = true;
    }

    public PeerFile(String fileName, Peer node, String path) {
        this.filePath = Paths.get(path, fileName);
        this.fileName = fileName;
        this.node = node;
    }

    public String getFileName() {
        return fileName;
    }

    public synchronized InputStream getContentsStream() {
        if (!isLocal) return null;
        try {
            return new FileInputStream(filePath.toString());
        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public synchronized OutputStream getDestinationStream() throws IOException {
        File file = new File(filePath.toString());
        if (!file.exists()) {
            file.createNewFile();
        }
        return new FileOutputStream(file);
    }

    @Deprecated
    public synchronized void writeToDisk(String path) throws IOException {
        if (contents == null || isLocal) return;
        this.filePath = Paths.get(path, fileName);
        ByteArrayInputStream in = new ByteArrayInputStream(contents);
        Files.copy(in, filePath);
        contents = null;
        isLocal = true;
        node = null;
    }

    public Peer getNode() {
        return node;
    }

    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeerFile)) return false;

        PeerFile peerFile1 = (PeerFile) o;

        if (!fileName.equals(peerFile1.fileName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }

    public boolean isPreview() {
        return contents == null;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
