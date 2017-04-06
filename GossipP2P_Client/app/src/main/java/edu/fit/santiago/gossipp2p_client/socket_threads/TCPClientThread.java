package edu.fit.santiago.gossipp2p_client.socket_threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/19/2017.
 */

/**
 * Thread for client to connect to server using TCP
 */
public class TCPClientThread extends Thread {
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    ServerModel serverModel;
    byte[] message;

    public TCPClientThread (byte[] _message, ServerModel _serverModel) {
        serverModel = _serverModel;
        message = _message;
    }

    public void run () {
        //StringBuilder serverResponse = new StringBuilder();
        try {
            Socket socket = new Socket();
            SocketAddress sa = new InetSocketAddress(InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
            socket.connect(sa, 10000);

            //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            out.write((message + "\n").getBytes());
            out.flush();
            //serverResponse.append(reader.readLine());
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }