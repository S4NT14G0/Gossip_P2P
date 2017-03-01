import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServerThread extends Thread {

    Socket sock;
    String input;
    BufferedReader reader;
    OutputStreamWriter out;
    
    public TCPServerThread(Socket _sock) {
    	sock = _sock;
    	System.out.println("TCP Created");
    }
	
	public void run () {
		
        try {    
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new OutputStreamWriter(sock.getOutputStream());
			
			out.write("Hello from concurrent!\n");
			out.flush();
	
			//for(;;){
				input=reader.readLine();
				
				if(input==null) {
					//break;
				}
				
				out.write(input+"\n");
				out.flush();
			//}
		    sock.close();
		} catch(IOException e){
		    //e.printStackTrace();
		}
	}

}
