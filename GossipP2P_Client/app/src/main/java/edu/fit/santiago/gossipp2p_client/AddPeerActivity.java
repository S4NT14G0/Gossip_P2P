package edu.fit.santiago.gossipp2p_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.fit.santiago.gossipp2p_client.events.ServerResponseEvent;
import edu.fit.santiago.gossipp2p_client.messages.GossipMessage;
import edu.fit.santiago.gossipp2p_client.messages.LeaveMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeersQueryMessage;
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
    ServerModel serverModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peer);

        txtServerResponse = (TextView) findViewById(R.id.txtAPServerResponse);

        final EditText etPeerName = (EditText) findViewById(R.id.etPeerName);
        final EditText etPeerIP = (EditText) findViewById(R.id.etPeerAddress);
        final EditText etPeerPort = (EditText) findViewById(R.id.etPeerPort);

        serverModel = (ServerModel) getIntent().getSerializableExtra("ServerModel");

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
                // Set the date of the peer
                peerMessage.peerContactRecieved();

                // Send the message
                if (serverModel.getConnectionType() == 1) {
                    // Try to send message to the server
                    sendPeerMessageTCP(peerMessage.encode());
                } else {
                    sendPeerMessageUDP(peerMessage.encode());
                }
            }
        });

        final Button btnPeerQuery = (Button) findViewById(R.id.btnPeerQuery);

        btnPeerQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PeersQueryMessage peersQueryMessage = new PeersQueryMessage();

                if (serverModel.getConnectionType() == 1)
                    sendPeerMessageTCP(peersQueryMessage.encode());
                else
                    sendPeerMessageUDP(peersQueryMessage.encode());

            }
        });

        final Button btnLeaveMessage = (Button) findViewById(R.id.btnLeaveMessage);

        btnLeaveMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText peerRemovedName = (EditText) findViewById(R.id.etRemovePeer);
                String peerName = peerRemovedName.getText().toString();

                LeaveMessage leaveMessage = new LeaveMessage();
                leaveMessage.setName(peerName);

                if (serverModel.getConnectionType() == 1)
                    sendPeerMessageTCP(leaveMessage.encode());
                else
                    sendPeerMessageUDP(leaveMessage.encode());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIncomingServerMessageEvent(ServerResponseEvent event) {
        txtServerResponse.setText(txtServerResponse.getText() + "\n" + event.message);
    }

    private void sendPeerMessageTCP (byte[] peerMessage) {
        TCPClientThread tcpClientThread = new TCPClientThread(peerMessage, serverModel, true);
        tcpClientThread.start();
    }

    private void sendPeerMessageUDP (byte[] peerMessage) {
        UDPClientThread udpClientThread = new UDPClientThread(peerMessage, serverModel, true);
        udpClientThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

}