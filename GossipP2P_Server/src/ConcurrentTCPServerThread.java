import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class ConcurrentTCPServerThread extends Thread {

    Socket sock;
    //StringBuffer input = null;

    InputStream in;
    OutputStream out ;

    byte[] byteBuffer = new byte[1024];
    int recvMsgSize;

    public ConcurrentTCPServerThread(Socket _sock) throws UnknownHostException {
    	sock = _sock;
    	System.out.println("TCP Created @ " + sock.getInetAddress().getLocalHost());
    }

	public void run () {
        try {
        	// Setup the streams
            in = sock.getInputStream();
            out = sock.getOutputStream();

            // Print to console to show which port we're connected too
            System.out.println("Handling client at " +
                    sock.getInetAddress().getHostAddress() + " on port " +
                    sock.getPort());
            
            String input = "";
            
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                input += new String (byteBuffer, "UTF-8").replace("\n", "").trim();
                byteBuffer = new byte[1024];
                
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
            
			sock.close();

		} catch(IOException e){
		    e.printStackTrace();
		}
	}

	private void handleMessage (String currentMessage) throws IOException {
		synchronized (Database.getInstance()) {
	        // Try to identify the message from the stream
	        Message inputMessage = Message.identifyMessage(currentMessage);

	        // Check what type of message was sent
	        if (inputMessage instanceof GossipMessage) {
	            out.write("Gossip Message Received\n".getBytes());
	            out.flush();
	            System.out.println("Received Gossip Message");
	            MessageHandler.HandleGossipMessage(inputMessage);
	        } else if (inputMessage instanceof PeerMessage) {
	            out.write("Add Peer Message Received\n".getBytes());
	            out.flush();
	            System.out.println("Received Peer Message");
	            MessageHandler.HandlePeerMessage(inputMessage);
	        } else if (inputMessage instanceof PeersListMessage) {
	            out.write((Database.getInstance().getPeersList().toString() + "\n").getBytes());
	            out.flush();
	            System.out.println("Peers List Requested");
	        } else if (inputMessage instanceof ErrorMessage) {
	            out.write("Error message received\n".getBytes());
	            out.flush();
	            System.out.println("Error");
	        }
		}
	}

}
