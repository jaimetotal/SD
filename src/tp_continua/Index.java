package tp_continua;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 17-11-2013
 * Time: 23:31
 * Student Number: 8090309
 */
public class Index implements Serializable {

    private List<String> filesName;

    public Index(List<PeerFile> peerFiles) {
        filesName = new ArrayList<String>();
        for (PeerFile peerFile : peerFiles) {
            if (peerFile.isLocal()) {
                this.filesName.add(peerFile.getFileName());
            }
        }
    }

    public List<String> getFilesName() {
        return filesName;
    }
}
