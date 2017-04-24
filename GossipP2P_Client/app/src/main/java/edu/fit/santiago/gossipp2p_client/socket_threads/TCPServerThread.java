package edu.fit.santiago.gossipp2p_client.socket_threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.fit.santiago.gossipp2p_client.MyApplication;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.database.PeerDaoImpl;
import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;
import edu.fit.santiago.gossipp2p_client.messages.Message;
import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.TCPServerMessageHandler;

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
            IncomingServerMessageEvent.postEventBusMessage("Handling client at " +
                    sock.getInetAddress().getHostAddress() + " on port " +
                    sock.getPort());

            int messageLen = in.read(byteBuffer);
            if (messageLen <= 0) return;

            Decoder decoder = new Decoder(byteBuffer, 0, messageLen);

            if (!decoder.fetchAll(in)) {
                IncomingServerMessageEvent.postEventBusMessage("Buffer too small or stream closed");
                return;
            }

            Message unidentifiedMessage = Message.identifyMessage(decoder);

            // Check if peer is known
            PeerDaoImpl peerDaoImpl = new PeerDaoImpl(MyApplication.getAppContext());
            PeerMessage peerMessage = peerDaoImpl.findPeerByInetAddress(sock.getInetAddress().getHostAddress());

            // If we know this peer update it's last seen time
            if (peerMessage != null) {
                peerMessage.peerContactRecieved();
                // Update the peer's contact date in db
                peerDaoImpl.updatePeerMessage(peerMessage);
            }

            new TCPServerMessageHandler().handleMessage(unidentifiedMessage, out);

        }
        // Exception thrown when network timeout occurs
        catch (InterruptedIOException oe)
        {
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            IncomingServerMessageEvent.postEventBusMessage ("Remote host timed out during read operation");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
