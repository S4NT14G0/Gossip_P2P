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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread for an iterative TCP Gossip Server.
 * @author sroig2013@my.fit.edu
 *
 */
public class IterativeTCPServerThread extends Thread {
	
	int port;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    
    /**
     * Construct a new iterative TCP Gossip Server.
     * @param _port Port that server will listen on.
     */
	public IterativeTCPServerThread (int _port) {
		this.port = _port;
	}
	
	/**
	 * Run the thread.
	 */
	public void run () {
		try {
			// Create a new server socket
			ServerSocket serverSocket = new ServerSocket(port);	
			System.out.println(serverSocket.getInetAddress().getLocalHost());	
					
			// Keep accepting connections
		    for (;;) {
		    	//Accept client connection
		        Socket sock = serverSocket.accept();
		      
		        // Create a buffered reader to read out of the sock buffer
			    reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			    // Create an output stream writer so we can write to sock's output
			    out = new OutputStreamWriter(sock.getOutputStream());	
			    
			    // Print to console to show which port we're connected too
		        System.out.println("Handling client at " +
		          sock.getInetAddress().getHostAddress() + " on port " +
		               sock.getPort());
		      
		        // While the reader isn't giving us nulls echo the user input
				while ((input = reader.readLine()) != null) {
					
					// Try to identify the message from the stream
					Message inputMessage = Message.identifyMessage(input);
					
					// Check what type of message was sent
					if (inputMessage instanceof GossipMessage) {
						//out.write("Gossip Message Received\n");
						//out.flush();
						System.out.println("Received Gossip Message");
						MessageHandler.HandleGossipMessage(inputMessage);
					} else if (inputMessage instanceof PeerMessage) {
						//out.write("Add Peer Message Received\n");
						//out.flush();
						System.out.println("Received Peer Message");
						MessageHandler.HandlePeerMessage(inputMessage);
					} else if (inputMessage instanceof PeersListMessage) {
						out.write(Database.getInstance().getPeersList().toString());
						out.flush();
						System.out.println("Peers List Requested");
						//MessageHandler.HandlePeersListMessage(sock.getPort(), sock.getInetAddress().getHostAddress());
					} else if (inputMessage instanceof ErrorMessage) {
						//out.write("Error message received\n");
						//out.flush();
						System.out.println("Error");
					}
					
				}
	
		        sock.close();  
		    }
		} catch (Exception e) {
			
		}
	}
}