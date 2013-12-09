package tp_continua.common;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logical view over OS filesystem and remote files
 */
public class FileSystem {

    private final InternalLogger logger;
    private CopyOnWriteArrayList<PeerFile> peerFiles;
    private String path;

    public FileSystem(String path) {
        logger = InternalLogger.getLogger(this.getClass());
        this.path = path;
        peerFiles = new CopyOnWriteArrayList<PeerFile>();
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                //Filters for files in only the sub folder
                if (file.isFile()) {
                    peerFiles.add(new PeerFile(file.getAbsolutePath()));
                }
            }
        }
    }

    public PeerFile getFileByName(String fileName) {
        logger.info("Searching for file %s.", fileName);
        for (PeerFile peerFile : peerFiles) {
            if (peerFile.getFileName().equals(fileName)) {
                logger.info("File %s found.", fileName);
                return peerFile;
            }
        }
        logger.info("File %s not found.", fileName);
        return null;
    }

    public void addFile(PeerFile peerFile) {
        if (!peerFiles.contains(peerFile)) {
            logger.info("Adding file %s from %s.", peerFile, peerFile.getNode());
            peerFiles.add(peerFile);
        } else {
            logger.info("File %s from %s already exists locally.", peerFile, peerFile.getNode());
        }
    }

    /**
     * All available files in filesystem
     *
     * @return
     */
    public Collection<PeerFile> getLocalFiles() {
        ArrayList<PeerFile> list = new ArrayList<>();
        for (PeerFile f : peerFiles) {
            if (f.isLocal()) {
                list.add(f);
            }
        }
        return list;
    }

    /**
     * @return Files available to download in remote Peers
     */
    public Collection<PeerFile> getRemoteFiles() {
        ArrayList<PeerFile> list = new ArrayList<>();
        for (PeerFile f : peerFiles) {
            if (!f.isLocal()) {
                list.add(f);
            }
        }
        return list;
    }

    public Index getIndex() {
        return new Index(getLocalFiles());
    }

    /**
     * Adds a remote index to the file system, to display them as available files to download
     *
     * @param source
     * @param index
     */
    public void addRemoteIndex(Peer source, Index index) {
        for (String fileName : index.getFilesName()) {
            PeerFile peerFile = new PeerFile(fileName, source, path);
            addFile(peerFile);
        }
    }


    /**
     * Adds file to filesystem if not added already and writes its contents to the disk, making it local
     *
     * @param source File to be saved
     * @throws IOException Error reported by the system when trying to save the file
     */
    @Deprecated
    public void updateFile(PeerFile source) throws IOException {
        if (!peerFiles.contains(source)) {
            peerFiles.add(source);
        }

        logger.info("Saving file %s to disk.", source);
        if (!source.isLocal() && !source.isPreview()) {
            source.writeToDisk(path);
        }
    }
}
