/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2017 
                Author:  sroig2013@my.fit.edu
                Florida Tech, Computer Science
   
       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.
   
      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.
  
      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */


import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Class to handle actions for specific message types.
 * @author sroig2013@my.fit.edu
 *
 */
public class MessageHandler {

	/**
	 * Handles gossip messages.  Store message in database and broadcast it to all known peers.
	 * @param _gossipMessage Gossip message to handle.
	 */
	public static void HandleGossipMessage(Message _gossipMessage) {
		// 1. Check DB to see if message is already known
		if (Database.getInstance().handleGossipMessage((GossipMessage)_gossipMessage)) {
			// 2. If known discard message and print DISCARDED on standard error
			System.err.println("DISCARDED");
		} else {
			// Get list of all peers
			PeersListMessage peersList = Database.getInstance().getPeersList();
			// Broadcast the gossipMessage to everyone on peers list
			broadcastGossipMessageUDP (peersList, (GossipMessage) _gossipMessage);
			broadcastGossipMesssageTCP (peersList, (GossipMessage) _gossipMessage);
			// Print the message on the standard error
			System.err.println(_gossipMessage.toString());
		}
	}
	
	/**
	 * Hanldes peer message. Sends message to database to be processed.
	 * @param _peerMessage Peer message to handle.
	 */
	public static void HandlePeerMessage (Message _peerMessage) {
		// 1. Check if DB knows this peer
		// 2. If it doesn't then store the peer
		// 3. If it does then update the address
		Database.getInstance().handlePeerMessage((PeerMessage) _peerMessage);
	}
	
	/**
	 * Broadcast gossip message to all known peers using TCP.
	 * @param peers List of all known peers.
	 * @param gossipMessage Message to be broadcasted.
	 */
	private static void broadcastGossipMesssageTCP (PeersListMessage peers, GossipMessage gossipMessage) {

		for (PeerMessage peer : peers.getPeers()) {
			try {
				Socket socket = new Socket();//new Socket (peer.getIpAddress(), peer.getPortNumber());
				SocketAddress sa = new InetSocketAddress(InetAddress.getByName(peer.getIpAddress()), peer.getPortNumber());
				socket.connect(sa, 1000);
				OutputStream out = socket.getOutputStream();
				
				out.write(gossipMessage.toString().getBytes());
				out.flush();
				
				socket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Couldn't connect to peer: " + peer.getPeerName());
			}
		}
		
	}
	
	/**
	 * Broadcast gossip message to all known peers using UDP.
	 * @param peers List of all known peers.
	 * @param gossipMessage Message to be broadcasted.
	 */
	private static void broadcastGossipMessageUDP (PeersListMessage peers, GossipMessage gossipMessage) {
		for (PeerMessage peer : peers.getPeers()) {
			try {
				DatagramSocket ds = new DatagramSocket();
				byte[] data = new byte[1000];
				data = gossipMessage.toString().getBytes();
				DatagramPacket packet = new DatagramPacket (data, data.length, InetAddress.getByName(peer.getIpAddress()), peer.getPortNumber());
				ds.send(packet);
				ds.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
