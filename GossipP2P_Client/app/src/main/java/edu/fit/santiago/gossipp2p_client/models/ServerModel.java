package edu.fit.santiago.gossipp2p_client.models;

/**
 * Created by santiago on 3/19/17.
 */

/**
 * Represents the server options on main activity
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

    /**
     * Set the server's options
     * @param _ipAddress IP Address to the server
     * @param _port Port that the server is listening on
     * @param _connectionType Connection type 1 == tcp, 2 == udp
     */
    public void setServer (String _ipAddress, Integer _port, Integer _connectionType) {
        this.ipAddress = _ipAddress;
        this.port = _port;
        this.connectionType = _connectionType;
    }

    /**
     *
     * @return Returns current server's IP
     */
    public String getIpAddress () {
        return this.ipAddress;
    }

    /**
     *
     * @return Returns current server's port
     */
    public Integer getPort () {
        return this.port;
    }

    /**
     *
     * @return Returns current server's connection type
     */
    public Integer getConnectionType () {
        return this.connectionType;
    }
}
