package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/23/2017.
 */

/**
 * Thread for client to connect to server using UDP
 */
public class UDPClientThread extends AsyncTask<String, Void, String> {
    String ipAddress;
    int port;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    TextView txtServerResponse;
    ServerModel serverModel;

    /**
     * Constructor for UDP thread
     * @param serverResponse Text view that the client should place server response text into.
     */
    public UDPClientThread (TextView serverResponse, ServerModel _serverModel) {
        txtServerResponse = serverResponse;
        serverModel = _serverModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... messages) {
        StringBuilder serverResponse = new StringBuilder();
        try {

            DatagramSocket ds = new DatagramSocket();
            byte[] data = new byte[1000];
            data = (messages[0] + "\n").getBytes();
            DatagramPacket packet = new DatagramPacket (data, data.length, InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
            ds.send(packet);

            byte buffer[] = new byte[1000];

            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            ds.receive(incomingPacket);     // Receive packet from client
            // Get the string message out of the datagram packet
            String input = new String(incomingPacket.getData(), "UTF-8");
            serverResponse.append(input);
            ds.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverResponse.toString();
    }

    @Override
    protected void onPostExecute(final String s) {
        txtServerResponse.setText(txtServerResponse.getText().toString() + "\n" + s);
    }
}
