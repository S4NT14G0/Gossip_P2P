package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;

/**
 * Created by Santiago on 4/4/2017.
 */

public class ServerStartThread extends AsyncTask<Void, Void, Void>{
    String hostname;

    ServerStartThread (String _hostname) {
        hostname = _hostname;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Socket sock;

        try {
            if (hostname == null)
                hostname = "0.0.0.0";
            ServerSocket serverSocket = new ServerSocket();
            int port = 3333;
            serverSocket.bind(new InetSocketAddress(hostname, port));
            EventBus.getDefault().post(new IncomingServerMessageEvent("TCP Conn: " + hostname + ":" + port));

            DatagramSocket dgSocket = new DatagramSocket(null);
            dgSocket.bind(new InetSocketAddress(hostname,port));

            EventBus.getDefault().post(new IncomingServerMessageEvent("UDP Conn: " + dgSocket.getLocalAddress() + ":" + dgSocket.getLocalPort()));

            new UDPServerThread(dgSocket).start();

            for (;;) {
                sock = serverSocket.accept();
                new TCPServerThread(sock).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
