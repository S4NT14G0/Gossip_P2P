package edu.fit.santiago.gossipp2p_client.models;

/**
 * Created by santiago on 3/19/17.
 */

public class ServerModel {
    private String ipAddress;
    int port;
    int connectionType;

    private static final ServerModel ourInstance = new ServerModel();

    public static ServerModel getInstance() {
        return ourInstance;
    }

    private ServerModel() {
    }

    public void setServer (String _ipAddress, int _port, int _connectionType) {
        this.ipAddress = _ipAddress;
        this.port = _port;
        this.connectionType = _connectionType;
    }

    public String getIpAddress () {
        return this.ipAddress;
    }

    public int getPort () {
        return this.port;
    }

    public int getConnectionType () {
        return this.connectionType;
    }
}
