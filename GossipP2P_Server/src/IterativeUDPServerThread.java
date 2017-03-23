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

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Thread for an iterative UDP Gossip Server.
 * @author sroig2013@my.fit.edu
 *
 */
public class IterativeUDPServerThread extends Thread {
	int port;
	
    /**
     * Construct a new iterative UDP Gossip Server.
     * @param _port Port that server will listen on.
     */
	public IterativeUDPServerThread(int _port) {
		this.port = _port;
	}
	
	/**
	 * Run the thread.
	 */
	public void run () {

		try  {
	      DatagramSocket socket = new DatagramSocket(port);
	      
	      for (;;) {  // Run forever, receiving and echoing datagrams
	    	  byte buffer[] = new byte[1000];
	    	  
	    	  DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
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
	    		  MessageHandler.HandleGossipMessage(inputMessage);
	    	  } else if (inputMessage instanceof PeerMessage) {
	    		  // Log we received an add peer message
	    		  System.out.println("Received Peer Message");
	    		  MessageHandler.HandlePeerMessage(inputMessage);
	    	  } else if (inputMessage instanceof PeersListMessage) {
	    		  System.out.println("Peers List Requested");
	    		  buffer = Database.getInstance().getPeersList().toString().getBytes();
	    		  packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
	    		  socket.send(packet);
				//MessageHandler.HandlePeersListMessage(packet.getPort(), packet.getAddress().getHostAddress());
	    	  } else if (inputMessage instanceof ErrorMessage) {
	    		  System.out.println("Error");
	    	  }
			
	    	  //packet.setData("Add Peer Message Received\n".getBytes());
	    	  //socket.send(packet);
	    	  packet.setLength(buffer.length);
      		}			
		} catch (Exception e) {
			
		}
	}
}