package edu.fit.santiago.gossipp2p_client.models;

/**
 * Created by santiago on 3/19/17.
 */

public class ServerModel {
    private String ipAddress;
    Integer port;
    Integer connectionType;

    private static final ServerModel ourInstance = new ServerModel();

    public static ServerModel getInstance() {
        return ourInstance;
    }

    private ServerModel() {
    }

    public void setServer (String _ipAddress, Integer _port, Integer _connectionType) {
        this.ipAddress = _ipAddress;
        this.port = _port;
        this.connectionType = _connectionType;
    }

    public String getIpAddress () {
        return this.ipAddress;
    }

    public Integer getPort () {
        return this.port;
    }

    public Integer getConnectionType () {
        return this.connectionType;
    }
}
