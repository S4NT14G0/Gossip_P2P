package edu.fit.santiago.gossipp2p_client.models;

/**
 * Created by santiago on 3/19/17.
 */

class ServerModel {
    private static final ServerModel ourInstance = new ServerModel();

    static ServerModel getInstance() {
        return ourInstance;
    }

    private ServerModel() {
    }
}
