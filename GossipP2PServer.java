import gossipp2p.serverthreads.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class GossipP2PServer {

    public static void main (String args[]) {
    	//runConcurrentServer(3333);
    	runIterativeServer(3333);
    }
    
    static void runConcurrentServer(int port) {
    	Socket sock;
    	
    	try {
    		ServerSocket serverSocket = new ServerSocket(port);
    		DatagramSocket dgSocket = new DatagramSocket(port);
    		
    		for (;;) {
    			
    			// change to run for iterative
				new ConcurrentUDPServerThread(dgSocket).start();
				sock = serverSocket.accept();
				new ConcurrentTCPServerThread(sock).start();
    			
    		}
    	} catch (Exception e) {
    		
    	}    	
    }
    
    static void runIterativeServer(int port) {
    	
    	try {
			new IterativeTCPServerThread (port).start();
			new IterativeUDPServerThread (port).start();

    	} catch (Exception e) {
    		
    	}    	
    }
    
}



