package edu.fit.santiago.gossipp2p_client.messages;

import java.io.IOException;
import java.io.OutputStream;

import edu.fit.santiago.gossipp2p_client.MyApplication;
import edu.fit.santiago.gossipp2p_client.database.MessageDaoImpl;
import edu.fit.santiago.gossipp2p_client.database.PeerDaoImpl;
import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;
import edu.fit.santiago.gossipp2p_client.socket_threads.TCPClientThread;
import edu.fit.santiago.gossipp2p_client.socket_threads.UDPClientThread;

/**
 * Created by Santiago on 4/6/2017.
 */

public class TCPServerMessageHandler {

    public void handleMessage (Message message, OutputStream out) throws IOException {
        if (message instanceof GossipMessage) {
            handleGossipMessage((GossipMessage) message, out);
        } else if (message instanceof PeerMessage) {
            handlePeerMessage((PeerMessage) message, out);
        } else if (message instanceof PeersQueryMessage) {
            handlePeersQueryMessage(out);
        }
    }

    private static void handleGossipMessage(GossipMessage gossipMessage, OutputStream out) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage(gossipMessage.toString());

        ResponseMessage responseMessage = new ResponseMessage("Received Gossip Message");

        out.write(responseMessage.encode());
        out.flush();

        // Check if gossip message is known
        // if it isn't get all of the known peers from DB and send them gossip message over UDP and TCP
        MessageDaoImpl messageDaoImpl = new MessageDaoImpl(MyApplication.getAppContext());

        if (!messageDaoImpl.isExistingMessage(gossipMessage)) {
            PeersAnswerMessage peersAnswerMessage = new PeerDaoImpl(MyApplication.getAppContext()).getAllPeers();

             for(PeerMessage peer : peersAnswerMessage.peers) {
                 ServerModel server = new ServerModel(peer.getIpAddress(), peer.getPortNumber());

                 TCPClientThread tcpClientThread = new TCPClientThread(gossipMessage.encode(), server, false);
                 tcpClientThread.start();

                 UDPClientThread udpClientThread = new UDPClientThread(gossipMessage.encode(), server, false);
                 udpClientThread.start();
            }
        }
    }

    private void handlePeerMessage (PeerMessage peerMessage, OutputStream out) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage(peerMessage.toString());
        ResponseMessage responseMessage = new ResponseMessage("Received Peer Message");

        out.write(responseMessage.encode());
        out.flush();

        // Update or insert peer into database
        PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());
        peerDaoImpl.updatePeerMessage(peerMessage);


    }

    private void handlePeersQueryMessage (OutputStream out) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage("Received Peer Query Message");

        // Send a Peers Answer message back to client on OutputStream
        // Update or insert peer into database
        PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());

        out.write(peerDaoImpl.getAllPeers().encode());
        out.flush();
    }

}
