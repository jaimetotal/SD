package tp_continua;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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

    public PeerFile(String fileName) {
        this.fileName = fileName;
        this.isLocal = true;
    }

    public PeerFile(String fileName, Peer node) {
        this.fileName = fileName;
        this.node = node;
    }

    public String getFileName() {
        return fileName;
    }

    public synchronized byte[] getContents() throws IOException {
        if (isLocal) {
            return Files.readAllBytes(Paths.get(fileName));
        }
        return contents;
    }

    public synchronized void setContents(byte[] contents) {
        this.contents = contents;
    }

    public synchronized void writeToDisk(String path) throws IOException {
        if (contents == null || isLocal) return;
        Path destination = Paths.get(path, fileName);
        ByteArrayInputStream in = new ByteArrayInputStream(contents);
        Files.copy(in, destination);
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
        int result = fileName.hashCode();
        result = 31 * result + (contents != null ? Arrays.hashCode(contents) : 0);
        result = 31 * result + node.hashCode();
        result = 31 * result + (isLocal ? 1 : 0);
        return result;
    }

    public boolean isPreview() {
        return contents == null;
    }
}
