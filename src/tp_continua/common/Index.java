package tp_continua.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Indexer of available files to download
 */
public class Index implements Serializable {

    private List<String> filesName;

    public Index(Collection<PeerFile> peerFiles) {
        filesName = new ArrayList<String>();
        for (PeerFile peerFile : peerFiles) {
            this.filesName.add(peerFile.getFileName());
        }
    }

    public List<String> getFilesName() {
        return filesName;
    }
}
