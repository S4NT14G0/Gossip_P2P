import gossipp2p.messages.*;
import gossipp2p.serverthreads.*;
import java.net.*;
import java.io.*;

public class GossipP2PServer {

    public static void main (String args[]) {
    	Socket sock;
    	
    	Message test = new GossipMessage(null, null, null);
    	
    	try {
    		ServerSocket serverSocket = new ServerSocket(3333,5);
    		DatagramSocket dgSocket = new DatagramSocket(3333);
    		
    		for (;;) {
				new UDPServerThread(dgSocket).start();
				sock = serverSocket.accept();
				new TCPServerThread(sock).start();
    			
    		}
    	} catch (Exception e) {
    		
    	}
    }
}