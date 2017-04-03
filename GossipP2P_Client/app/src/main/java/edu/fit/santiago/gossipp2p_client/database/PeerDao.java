package edu.fit.santiago.gossipp2p_client.database;

import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeersListMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public interface PeerDao {
    public void updatePeerMessage (PeerMessage _peerMessage);
    public void deletePeer (String peerName);
    public PeersListMessage getAllPeers();
}
