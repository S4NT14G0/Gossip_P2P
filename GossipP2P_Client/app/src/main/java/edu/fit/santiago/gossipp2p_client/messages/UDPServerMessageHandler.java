package edu.fit.santiago.gossipp2p_client.messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

public class UDPServerMessageHandler {

    public void handleMessage (Message message, DatagramSocket ds, InetAddress clientAddress, int port) throws IOException {
        if (message instanceof GossipMessage) {
            handleGossipMessage((GossipMessage) message, ds, clientAddress, port);
        } else if (message instanceof PeerMessage) {
            handlePeerMessage((PeerMessage) message, ds, clientAddress, port);
        } else if (message instanceof PeersQueryMessage) {
            handlePeersQueryMessage(ds, clientAddress, port);
        }
    }

    private static void handleGossipMessage(GossipMessage gossipMessage, DatagramSocket ds,InetAddress clientAddress, int port) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage(gossipMessage.toString());
        ResponseMessage responseMessage = new ResponseMessage("Received Gossip Message");

        byte[] responseBytes = responseMessage.encode();
        DatagramPacket datagramPacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, port);
        ds.send(datagramPacket);

        // Check if gossip message is known
        // if it isn't get all of the known peers from DB and send them gossip message over UDP and TCP
        MessageDaoImpl messageDaoImpl = new MessageDaoImpl(MyApplication.getAppContext());

        if (!messageDaoImpl.isExistingMessage(gossipMessage)) {
            messageDaoImpl.insertGossipMessage(gossipMessage);
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

    private void handlePeerMessage (PeerMessage peerMessage, DatagramSocket ds, InetAddress clientAddress, int port) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage(peerMessage.toString());
        ResponseMessage responseMessage = new ResponseMessage("Received Peer Message");

        byte[] responseBytes = responseMessage.encode();
        DatagramPacket datagramPacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, port);
        ds.send(datagramPacket);

        // Update or insert peer into database
        PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());
        peerDaoImpl.updatePeerMessage(peerMessage);


    }

    private void handlePeersQueryMessage (DatagramSocket ds, InetAddress clientAddress, int port) throws IOException {
        IncomingServerMessageEvent.postEventBusMessage("Received Peer Query Message");

        // Send a Peers Answer message back to client on OutputStream
        // Update or insert peer into database
        PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());

        byte[] responseBytes = peerDaoImpl.getAllPeers().encode();
        DatagramPacket datagramPacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, port);
        ds.send(datagramPacket);
    }
}
