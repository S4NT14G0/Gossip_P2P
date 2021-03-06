package edu.fit.santiago.gossipp2p_client.database;

import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeersAnswerMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public interface PeerDao {
    public void updatePeerMessage (PeerMessage _peerMessage);
    public void deletePeer (String peerName);
    public PeersAnswerMessage getAllPeers();
    public PeerMessage findPeerByInetAddress (String ipAddress);
    public PeerMessage getPeerByName (String name);
}
