package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;

/**
 * Thread for client to connect to server using TCP
 */
public class TCPServerThread extends Thread {

    Socket sock;
    InputStream in;
    OutputStream out ;
    byte[] byteBuffer = new byte[1024];
    int recvMsgSize;

    /**
     * Constructor for TCP thread
     * @param _socket Text view that the client should place server response text into.
     */
    public TCPServerThread (Socket _socket) throws UnknownHostException {
        sock = _socket;
    }

    public void run() {
        try {
            sock.setSoTimeout(10000);

            // Setup the streams
            in = sock.getInputStream();
            out = sock.getOutputStream();

            // Print to console to show which port we're connected too
            postEventBusMessage("Handling client at " +
                    sock.getInetAddress().getHostAddress() + " on port " +
                    sock.getPort());

            String input = "";

            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                input = new String(byteBuffer, "UTF-8").replace("\n", "").trim();
                postEventBusMessage(input);
                byteBuffer = new byte[1024];
                out.write((input + "\n").getBytes());
                out.flush();
            }
        }
        // Exception thrown when network timeout occurs
        catch (InterruptedIOException iioe)
        {
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            postEventBusMessage ("Remote host timed out during read operation");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postEventBusMessage (String message) {
        EventBus.getDefault().post(new IncomingServerMessageEvent(message));
    }

}
