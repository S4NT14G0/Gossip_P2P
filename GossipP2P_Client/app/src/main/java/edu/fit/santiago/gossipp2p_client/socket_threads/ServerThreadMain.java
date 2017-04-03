package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.net.Socket;

/**
 * Created by Santiago on 4/2/2017.
 */

public class ServerThreadMain extends AsyncTask<Object, Object, Void> {

    TextView serverIncoming;
    int port;


    public ServerThreadMain (TextView _serverIncoming, int _port) {
        serverIncoming = _serverIncoming;
        port = _port;
    }

    @Override
    protected Void doInBackground(Object... params) {
        // Listen here for incoming connections
        Socket sock;

        try {
            new TCPServerThread(serverIncoming, sock).execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
