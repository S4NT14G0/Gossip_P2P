package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Thread for client to connect to server using TCP
 */
public class TCPServerThread extends AsyncTask<String, Void, String> {

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
        System.out.println("TCP Created @ " + _socket.getInetAddress().getLocalHost());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... messages) {
        try {
            // Setup the streams
            in = sock.getInputStream();
            out = sock.getOutputStream();

            // Print to console to show which port we're connected too
            System.out.println("Handling client at " +
                    sock.getInetAddress().getHostAddress() + " on port " +
                    sock.getPort());

            String input = "";

            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                input += new String(byteBuffer, "UTF-8").replace("\n", "").trim();
                byteBuffer = new byte[1024];
                out.write(byteBuffer);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(final String s) {
    }
}
