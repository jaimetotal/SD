package tp_continua;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class FileSystem {

    private CopyOnWriteArrayList<PeerFile> peerFiles;
    private String path;

    public FileSystem(String path) {
        this.path = path;
        peerFiles = new CopyOnWriteArrayList<PeerFile>();
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                peerFiles.add(new PeerFile(file.getName()));
            }
        }
    }

    public PeerFile getFileByName(String fileName) {
        for (PeerFile peerFile : peerFiles) {
            if (peerFile.getFileName().equals(fileName)) {
                return peerFile;
            }
        }
        return null;
    }

    public void addFile(PeerFile peerFile) {
        if (!peerFiles.contains(peerFile)) {
            peerFiles.add(peerFile);
        }
    }

    public Collection<PeerFile> listFiles() {
        //TODO check readonly collections
        return peerFiles;
    }

    public Index getIndex() {
        return new Index(peerFiles);
    }

    /**
     * Adds a remote index to the file system, to display them as available files to download
     *
     * @param source
     * @param index
     */
    public void addRemoteIndex(Peer source, Index index) {
        for (String fileName : index.getFilesName()) {
            PeerFile peerFile = new PeerFile(fileName, source);
            if (this.peerFiles.contains(peerFile)) {
                this.addFile(peerFile);
            }
        }
    }

    /**
     * Adds file to filesystem if not added already and writes its contents to the disk, making it local
     *
     * @param source File to be saved
     * @throws IOException Error reported by the system when trying to save the file
     */
    public void updateFile(PeerFile source) throws IOException {
        if (!peerFiles.contains(source)) {
            peerFiles.add(source);
        }
        if (!source.isLocal() && !source.isPreview()) {
            source.writeToDisk(path);
        }
    }
}
