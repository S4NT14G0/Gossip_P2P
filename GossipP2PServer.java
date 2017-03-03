import gossipp2p.serverthreads.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.sql.SQLException;

import gossipp2p.database.*;
public class GossipP2PServer {

    public static void main (String args[]) {
    	//runConcurrentServer(3333);
    	try {
			Database.getInstance().initializeDatabase(args[1]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Database.getInstance().testDB();
    	
    	runIterativeServer(Integer.parseInt(args[0]));
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



