package tp_continua;


import java.io.File;
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

    public FileSystem() {
        peerFiles = new CopyOnWriteArrayList<PeerFile>();
    }

    public FileSystem(String path) {
        peerFiles = new CopyOnWriteArrayList<PeerFile>();
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                //TODO get content
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

    public void addRemoteIndex(Peer source, Index index) {
        for (String fileName : index.getFilesName()) {
            PeerFile peerFile = new PeerFile(fileName, null, source);
            if (this.peerFiles.contains(peerFile)) {
                this.addFile(peerFile);
            }
        }
    }

    //TODO check if required
/*
    public void updateFile(PeerFile remoteFile)
    {
        for (PeerFile localFile : peerFiles)
        {
            if(localFile.equals(remoteFile))
            {
                peerFiles.remove(localFile);
                peerFiles.add(remoteFile);
                return;
            }
        }
        peerFiles.add(remoteFile);
    }*/
}
