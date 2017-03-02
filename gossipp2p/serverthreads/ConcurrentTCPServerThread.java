package gossipp2p.serverthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ConcurrentTCPServerThread extends Thread {

    Socket sock;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    
    public ConcurrentTCPServerThread(Socket _sock) {
    	sock = _sock;
    	System.out.println("TCP Created");
    }
	
	public void run () {
        try {    
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new OutputStreamWriter(sock.getOutputStream());
			
			out.write("Hello from concurrent!\n");
			out.flush();
	
			while ((input = reader.readLine()) != null) {
				out.write(input+"\n");
				out.flush();
			}
			
			sock.close();

		} catch(IOException e){
		    //e.printStackTrace();
		}
	}

}
