package gossipp2p.serverthreads;

import gossipp2p.messages.ErrorMessage;
import gossipp2p.messages.PeerMessage;
import gossipp2p.messages.GossipMessage;
import gossipp2p.messages.Message;
import gossipp2p.messages.PeersListMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class IterativeUDPServerThread extends Thread {
	int port;
	
	public IterativeUDPServerThread(int _port) {
		this.port = _port;
	}
	
	public void run () {

		try  {
	      DatagramSocket socket = new DatagramSocket(port);
	      DatagramPacket packet = new DatagramPacket(new byte[255], 255);
	      for (;;) {  // Run forever, receiving and echoing datagrams
	        socket.receive(packet);     // Receive packet from client
	        System.out.println("Handling client at " +
	          packet.getAddress().getHostAddress() + " on port " + packet.getPort());
	        
	        // Get the string message out of the datagram packet
			String input = new String(packet.getData(), "UTF-8");
			
			// Try to parse the message
			Message inputMessage = Message.identifyMessage(input);
			
			// Check what type of message was sent
			if (inputMessage instanceof GossipMessage) {
				// Log we've received a gossip message
				System.out.println("Received Gossip Message");
			} else if (inputMessage instanceof PeerMessage) {
				// Log we received an add peer message
				System.out.println("Received Peer Message");

			} else if (inputMessage instanceof PeersListMessage) {
				
			} else if (inputMessage instanceof ErrorMessage) {

			}
			
			//packet.setData("Add Peer Message Received\n".getBytes());
			socket.send(packet);
			packet.setLength(255);
	      }			
		} catch (Exception e) {
			
		}
	}
}