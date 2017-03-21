package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class TCPClientThread extends AsyncTask<String, Void, String> {

    String ipAddress;
    int port;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    TextView txtServerResponse;

    public TCPClientThread (TextView serverResponse) {
        txtServerResponse = serverResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... messages) {
        StringBuilder serverResponse = new StringBuilder();
        try {
            Socket socket = new Socket();
            SocketAddress sa = new InetSocketAddress(InetAddress.getByName(ServerModel.getInstance().getIpAddress()), ServerModel.getInstance().getPort());
            socket.connect(sa, 1000);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            out.write((messages[0] + "\n").getBytes());
            out.flush();
            serverResponse.append(reader.readLine());
            socket.close();

        } catch (Exception e) {

        }

        return serverResponse.toString();
    }

    @Override
    protected void onPostExecute(final String s) {
        txtServerResponse.setText(s);
    }
}

//    /**
//     * Construct a new iterative TCP Gossip Server.
//     * @param _port Port that server will listen on.
//     */
//    public TCPClientThread (String _ipAddress, int _port, String _message) {
//        this.ipAddress = _ipAddress;
//        this.port = _port;
//        this.message = _message;
//    }