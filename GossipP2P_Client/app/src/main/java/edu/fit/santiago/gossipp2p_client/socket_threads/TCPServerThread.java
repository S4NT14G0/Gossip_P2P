package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.net.ServerSocket;

/**
 * Created by Santiago on 4/2/2017.
 */

public class TCPServerThread extends AsyncTask <Void, Void, String> {

    TextView serverIncoming;
    ServerSocket socket;

    public TCPServerThread(TextView _serverIncoming, ServerSocket _socket) {
        serverIncoming = _serverIncoming;
        socket = _socket;
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }
}
