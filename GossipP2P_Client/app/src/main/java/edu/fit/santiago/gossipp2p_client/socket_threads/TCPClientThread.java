package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import edu.fit.santiago.gossipp2p_client.utils.HashString;

import edu.fit.santiago.gossipp2p_client.R;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/19/2017.
 */

/**
 * Thread for client to connect to server using TCP
 */
public class TCPClientThread extends AsyncTask<String, Void, String> {

    String ipAddress;
    int port;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    TextView txtServerResponse;
    ServerModel serverModel;

    /**
     * Constructor for TCP thread
     * @param serverResponse Text view that the client should place server response text into.
     */
    public TCPClientThread (TextView serverResponse, ServerModel _serverModel) {
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
            Socket socket = new Socket();
            SocketAddress sa = new InetSocketAddress(InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
            socket.connect(sa, 1000);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            out.write((messages[0] + "\n").getBytes());
            out.flush();
            serverResponse.append(reader.readLine());
            socket.close();

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