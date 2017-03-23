package edu.fit.santiago.gossipp2p_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.fit.santiago.gossipp2p_client.messages.GossipMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;
import edu.fit.santiago.gossipp2p_client.socket_threads.TCPClientThread;
import edu.fit.santiago.gossipp2p_client.socket_threads.UDPClientThread;
import edu.fit.santiago.gossipp2p_client.utils.HashString;

/**
 * Created by santiago on 3/19/17.
 */

/**
 * Activity for adding peers.
 */
public class AddPeerActivity extends AppCompatActivity {
    TextView txtServerResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peer);

        txtServerResponse = (TextView) findViewById(R.id.txtAPServerResponse);
        final EditText etPeerName = (EditText) findViewById(R.id.etPeerName);
        final EditText etPeerIP = (EditText) findViewById(R.id.etPeerAddress);
        final EditText etPeerPort = (EditText) findViewById(R.id.etPeerPort);

        final FloatingActionButton fabSendPeer = (FloatingActionButton) findViewById(R.id.fabSendPeer);
        fabSendPeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the peer name
                String peerName = etPeerName.getText().toString();
                // Get the peer IP
                String peerAddress = etPeerIP.getText().toString();
                // Get the peer port
                int peerPort = Integer.parseInt(etPeerPort.getText().toString());

                // Build peer message
                PeerMessage peerMessage = new PeerMessage(peerName, peerPort, peerAddress);

                // Send the message
                if (ServerModel.getInstance().getConnectionType() == 1) {
                    // Try to send message to the server
                    SendPeerMessageTCP(peerMessage.toString());
                } else {
                    SendPeerMessageUDP(peerMessage.toString());
                }
            }
        });
    }

    private void SendPeerMessageTCP (String peerMessage) {
        TCPClientThread tcpClientThread = new TCPClientThread(txtServerResponse);
        tcpClientThread.execute(peerMessage);
    }

    private void SendPeerMessageUDP (String peerMessage) {
        UDPClientThread udpClientThread = new UDPClientThread(txtServerResponse);
        udpClientThread.execute(peerMessage);
    }

}