package edu.fit.santiago.gossipp2p_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.socket_threads.TCPClientThread;
import edu.fit.santiago.gossipp2p_client.utils.HashString;


/**
 * Created by santiago on 3/18/17.
 */

public class GossipMessageActivity extends AppCompatActivity {

    EditText etGossipMessage;
    TextView txtServerResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip_message);

        etGossipMessage = (EditText) findViewById(R.id.etGossipMessage);
        txtServerResponse = (TextView) findViewById(R.id.txtGossipServerResponse);

        final FloatingActionButton fabSendGossip = (FloatingActionButton) findViewById(R.id.fabSendGossip);

        // Send message on message button.
        fabSendGossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Try to send message to the server
                SendGossipMessage();
            }
        });
    }

    private void SendGossipMessage () {
        TCPClientThread tcpClientThread = new TCPClientThread(txtServerResponse);

        String message = etGossipMessage.getText().toString();
        String shaEncodedMessage = HashString.getSHA256HashString(message);
        tcpClientThread.execute(message, shaEncodedMessage);
    }

}
