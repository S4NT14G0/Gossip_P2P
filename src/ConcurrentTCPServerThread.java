import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;


public class ConcurrentTCPServerThread extends Thread {

    Socket sock;
    //StringBuffer input = null;

    InputStream in;
    OutputStream out ;

    byte[] byteBuffer = new byte[1024];
    int recvMsgSize;

    public ConcurrentTCPServerThread(Socket _sock) {
    	sock = _sock;
    	System.out.println("TCP Created");
    }

	public void run () {
        try {

            in = sock.getInputStream();

            out = sock.getOutputStream();

//            byteBuffer = "Hello from concurrent!\n".getBytes();
//			out.write(byteBuffer, 0, byteBuffer.length);
//			out.flush();

            // Print to console to show which port we're connected too
            System.out.println("Handling client at " +
                    sock.getInetAddress().getHostAddress() + " on port " +
                    sock.getPort());

            String clientResponse = getClientResponse();

            while (!clientResponse.contains("%")){
                String extraResponse = getClientResponse();
                clientResponse += extraResponse;
            }

            String[] messageTokens = clientResponse.split("%");

            Queue<String> messageQueue = new LinkedList<String>();

            for (int i = 0; i < messageTokens.length; i++) {
                messageQueue.add(messageTokens[i] + "%");
            }

            while (messageQueue.size() > 0) {
                String currentMesage = messageQueue.remove();

                // Try to identify the message from the stream
                Message inputMessage = Message.identifyMessage(currentMesage);

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
                    out.write(Database.getInstance().getPeersList().toString().getBytes());
                    out.flush();
                    System.out.println("Peers List Requested");
                    //MessageHandler.HandlePeersListMessage(sock.getPort(), sock.getInetAddress().getHostAddress());
                } else if (inputMessage instanceof ErrorMessage) {
                    out.write("Error message received\n".getBytes());
                    out.flush();
                    System.out.println("Error");
                }
            }

			sock.close();

		} catch(IOException e){
		    e.printStackTrace();
		}
	}


	private String getClientResponse() throws IOException{

        while ((recvMsgSize = in.read(byteBuffer)) != -1) {
            return  new String (byteBuffer, "UTF-8");
        }
        return "";
    }
}
