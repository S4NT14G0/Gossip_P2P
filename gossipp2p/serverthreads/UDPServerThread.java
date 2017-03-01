package gossipp2p.serverthreads;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServerThread extends Thread {
	
	private static final int ECHOMAX = 255;
	DatagramSocket dgSocket;
	DatagramPacket packet;
	
	public UDPServerThread (DatagramSocket _dgSocket) {
		dgSocket = _dgSocket;
    	System.out.println("UDP Created");
	}
	
	public void run () {
		try{
			for (;;) {
				packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

				dgSocket.receive(packet);
				System.out.println("Handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
				String str = new String(packet.getData(), "UTF-8");
				System.out.println(str);
				dgSocket.send(packet);
				packet.setLength(ECHOMAX);				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

