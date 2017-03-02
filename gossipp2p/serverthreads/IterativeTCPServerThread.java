package gossipp2p.serverthreads;

import gossipp2p.messages.GossipMessage;
import gossipp2p.messages.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IterativeTCPServerThread extends Thread {
	
	int port;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    
	public IterativeTCPServerThread (int _port) {
		this.port = _port;
	}
	
	public void run () {
		try {
			// Create a new server socket
			ServerSocket serverSocket = new ServerSocket(port);		
					
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
					Message inputMessage = Message.identifyMessage(input);
					
					if (inputMessage instanceof GossipMessage) {
						out.write("Gossip Message Received\n");
						out.flush();
					}
					
					out.write(input+"\n");
					out.flush();
				}
	
		        sock.close();  
		    }
		} catch (Exception e) {
			
		}
	}
}