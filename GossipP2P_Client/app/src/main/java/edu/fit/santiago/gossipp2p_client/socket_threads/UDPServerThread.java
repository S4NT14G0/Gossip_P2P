package edu.fit.santiago.gossipp2p_client.socket_threads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import edu.fit.santiago.gossipp2p_client.MyApplication;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.database.PeerDao;
import edu.fit.santiago.gossipp2p_client.database.PeerDaoImpl;
import edu.fit.santiago.gossipp2p_client.messages.Message;
import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.UDPServerMessageHandler;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 4/4/2017.
 */

public class UDPServerThread extends Thread {
    private static final int MAXBUFFER = 1000;
    DatagramSocket dgSocket;
    DatagramPacket packet;
    byte[] byteBuffer = new byte[MAXBUFFER];

    public UDPServerThread (DatagramSocket _dgSocket) {
        dgSocket = _dgSocket;
    }

    public void run () {
        try{

            for (;;) {
                packet = new DatagramPacket(byteBuffer, byteBuffer.length);
                dgSocket.receive(packet);

                Decoder decoder = new Decoder(packet.getData(), packet.getOffset(), packet.getLength());

                Message unidentifiedMessage = Message.identifyMessage(decoder);

                // Check if peer is known
                PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());
                PeerMessage peerMessage = peerDaoImpl.findPeerByInetAddress(packet.getAddress().getHostAddress(), packet.getPort());

                // If we know this peer update it's last seen time
                if (peerMessage != null) {
                    peerMessage.peerContactRecieved();
                    // Update the peer's contact date in db
                    peerDaoImpl.updatePeerMessage(peerMessage);
                }

                new UDPServerMessageHandler().handleMessage(unidentifiedMessage, dgSocket, packet.getAddress(), packet.getPort());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
