package gossipp2p.serverthreads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class IterativeUDPServerThread extends Thread {
	int port;
	
	public IterativeUDPServerThread(int _port) {
		this.port = _port;
	}
	
	public void run () {

		try  {
	      DatagramSocket socket = new DatagramSocket(port);
	      DatagramPacket packet = new DatagramPacket(new byte[255], 255);
	      for (;;) {  // Run forever, receiving and echoing datagrams
	        socket.receive(packet);     // Receive packet from client
	        System.out.println("Handling client at " +
	          packet.getAddress().getHostAddress() + " on port " + packet.getPort());
	        socket.send(packet);       // Send the same packet back to client
	        packet.setLength(255); // Reset length to avoid shrinking buffer
	      }
	      /* NOT REACHED */  
			
		} catch (Exception e) {
			
		}
	}
}