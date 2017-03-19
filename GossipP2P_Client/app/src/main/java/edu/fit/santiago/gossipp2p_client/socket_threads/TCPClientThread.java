package edu.fit.santiago.gossipp2p_client.socket_threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/19/2017.
 */

public class TCPClientThread extends Thread {

    String ipAddress;
    int port;
    String message;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;

    /**
     * Construct a new iterative TCP Gossip Server.
     * @param _port Port that server will listen on.
     */
    public TCPClientThread (String _ipAddress, int _port, String _message) {
        this.ipAddress = _ipAddress;
        this.port = _port;
        this.message = _message;
    }

    /**
     * Run the thread.
     */
    public void run () {

        try {
            Socket socket = new Socket();
            SocketAddress sa = new InetSocketAddress(InetAddress.getByName(ServerModel.getInstance().getIpAddress()), ServerModel.getInstance().getPort());
            socket.connect(sa, 1000);
            OutputStream out = socket.getOutputStream();

            out.write(this.message.getBytes());
            out.flush();

            socket.close();
        } catch (Exception e) {

        }
    }
}
