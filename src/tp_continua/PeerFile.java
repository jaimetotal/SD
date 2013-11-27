package tp_continua;

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

    public PeerFile(String fileName, byte[] contents, Peer node) {
        this.fileName = fileName;
        this.contents = contents;
        this.node = node;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public Peer getNode() {
        return node;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean isPreview() {
        return contents == null;
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
}
