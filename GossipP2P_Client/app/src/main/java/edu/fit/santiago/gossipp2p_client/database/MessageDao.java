package edu.fit.santiago.gossipp2p_client.database;

import java.util.List;

import edu.fit.santiago.gossipp2p_client.messages.GossipMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public interface MessageDao {
    public void insertGossipMessage(GossipMessage gossipMessage);
    public boolean isExistingMessage (GossipMessage gossipMessage);
}
