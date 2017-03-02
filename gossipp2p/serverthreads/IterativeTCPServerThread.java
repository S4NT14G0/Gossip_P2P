package gossipp2p.serverthreads;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IterativeTCPServerThread extends Thread {
	int port;
	
	public IterativeTCPServerThread (int _port) {
		this.port = _port;
	}
	
	public void run () {
		try {
			
			ServerSocket serverSocket = new ServerSocket(port);
	
		    int recvMsgSize;   // Size of received message
		    byte[] byteBuffer = new byte[255];  // Receive buffer
	
		    for (;;) { // Run forever, accepting and servicing connections
		      Socket clntSock = serverSocket.accept();     // Get client connection
	
		      System.out.println("Handling client at " +
		        clntSock.getInetAddress().getHostAddress() + " on port " +
		             clntSock.getPort());
	
		      InputStream in = clntSock.getInputStream();
		      OutputStream out = clntSock.getOutputStream();
	
		      // Receive until client closes connection, indicated by -1 return
		      while ((recvMsgSize = in.read(byteBuffer)) != -1)
		        out.write(byteBuffer, 0, recvMsgSize);
	
		      clntSock.close();  // Close the socket.  We are done with this client!
		    }
		} catch (Exception e) {
			
		}
	}
}