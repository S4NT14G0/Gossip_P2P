package edu.fit.santiago.gossipp2p_client.database;

import edu.fit.santiago.gossipp2p_client.messages.GossipMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public interface MessageDao {
    public boolean updateGossipMessage (GossipMessage _gossipMessage);
}
