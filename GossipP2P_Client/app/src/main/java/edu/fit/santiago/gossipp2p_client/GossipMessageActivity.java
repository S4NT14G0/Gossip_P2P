package edu.fit.santiago.gossipp2p_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.models.ServerModel;
import edu.fit.santiago.gossipp2p_client.socket_threads.TCPClientThread;


/**
 * Created by santiago on 3/18/17.
 */

public class GossipMessageActivity extends AppCompatActivity {

    Socket socket;
    DatagramSocket dgSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip_message);
        final FloatingActionButton fabSendGossip = (FloatingActionButton) findViewById(R.id.fabSendGossip);

        fabSendGossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Try to send message to the server
                SendGossipMessage();
            }
        });
    }

    private void SendGossipMessage () {
        new TCPClientThread(ServerModel.getInstance().getIpAddress(), ServerModel.getInstance().getPort(), "PEERS?\\n").start();
    }

}
