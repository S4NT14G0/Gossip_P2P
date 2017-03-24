
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ConcurrentUDPServerThread extends Thread {
	
	private static final int MAXBUFFER = 1000;
	DatagramSocket dgSocket;
	DatagramPacket packet;
	byte[] byteBuffer = new byte[MAXBUFFER];
	
	public ConcurrentUDPServerThread (DatagramSocket _dgSocket) throws UnknownHostException {
		dgSocket = _dgSocket;
	}
	
	@SuppressWarnings("static-access")
	public void run () {
		try{
	    	System.out.println("UDP Created @ " + dgSocket.getInetAddress().getLocalHost());

	    	String input ="";
	    	
	    	for (;;) {
				packet = new DatagramPacket(byteBuffer, byteBuffer.length);
				dgSocket.receive(packet);
				input += new String(packet.getData(), "UTF-8").replace("\n", "").trim();
				
                // Check if a message is ready to execute
                if (Message.isReadyForParsing(input)) {
                	// Handle the message
                	handleMessage(input);
                	input = "";
                } else {
                	// Find how many times % and \\n exist in input to split apart each message
                	int slashNTokenCount = input.length() - input.replace("\\n", "").length() - 1;
                	int percentTokenCount = input.length() - input.replace("%", "").length();
                	
                	int endTokenCount = (slashNTokenCount > 0) ? slashNTokenCount + percentTokenCount : percentTokenCount;
                	
                	if (endTokenCount > 0) {
                		// Split up the messages based on their end characters
                		String[] messageTokens = input.split("\\\\n|%");
                		
                		// In this case we have an extra unfinished message at the end
                		if (messageTokens.length > endTokenCount) {
                			// Loop through the message tokens and handle them.
                			// Leave the last one there so it can be completed the next time the buffer hash input
                			for (int i = 0; i < messageTokens.length - 1; i++) {
                   				if (messageTokens[i].contains("PEERS?")) {
                   					messageTokens[i] += "\\n";
                				} else {
                					messageTokens[i] += "%";
                				}
                				
                				// Handle each message
                				handleMessage(messageTokens[i]);
                			}
                			
                			input = messageTokens[messageTokens.length - 1];
                		} else {
                			for (String message : messageTokens) {
                				// Put the end tokens back on each message
                				if (message.contains("PEERS?")) {
                					message += "\\n";
                				} else {
                					message += "%";
                				}
                				
                				// Handle each message
                				handleMessage(message);
                				
                				// reset the current input
                				input = "";
                			}
                		}
                		
                	}
                }								
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleMessage (String currentMessage) throws IOException {
		synchronized (Database.getInstance()) {
		  	  // Try to parse the message
		  	  Message inputMessage = Message.identifyMessage(currentMessage);
				
		  	  // Check what type of message was sent
		  	  if (inputMessage instanceof GossipMessage) {
		  		  // Log we've received a gossip message
		  		  System.out.println("Received Gossip Message");
		  		  byteBuffer = "Received Gossip Message\n".getBytes();
		  		  packet = new DatagramPacket(byteBuffer, byteBuffer.length, packet.getAddress(), packet.getPort());
		  		  dgSocket.send(packet);
		  		  MessageHandler.HandleGossipMessage(inputMessage);
		  	  } else if (inputMessage instanceof PeerMessage) {
		  		  // Log we received an add peer message
		  		  System.out.println("Received Peer Message");
		  		  byteBuffer = "Received Peer Message\n".getBytes();
		  		  packet = new DatagramPacket(byteBuffer, byteBuffer.length, packet.getAddress(), packet.getPort());
		  		  dgSocket.send(packet);
		  		  MessageHandler.HandlePeerMessage(inputMessage);
		  	  } else if (inputMessage instanceof PeersListMessage) {
		  		  System.out.println("Peers List Requested");
		  		  byteBuffer = (Database.getInstance().getPeersList().toString() + "\n").getBytes();
		  		  packet = new DatagramPacket(byteBuffer, byteBuffer.length, packet.getAddress(), packet.getPort());
		  		  dgSocket.send(packet);
		  	  } else if (inputMessage instanceof ErrorMessage) {
		  		  System.out.println("Error");
		  		  byteBuffer = "Received Error Message\n".getBytes();
		  		  packet = new DatagramPacket(byteBuffer, byteBuffer.length, packet.getAddress(), packet.getPort());
		  		  dgSocket.send(packet);
		  	  }
				
		  	  packet.setLength(byteBuffer.length);
		}
	}
}

