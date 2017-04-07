package edu.fit.santiago.gossipp2p_client.socket_threads;

import android.os.AsyncTask;
import android.widget.TextView;

import org.greenrobot.eventbus.util.AsyncExecutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.events.ServerResponseEvent;
import edu.fit.santiago.gossipp2p_client.messages.Message;
import edu.fit.santiago.gossipp2p_client.messages.PeersAnswerMessage;
import edu.fit.santiago.gossipp2p_client.messages.ResponseMessage;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/23/2017.
 */

/**
 * Thread for client to connect to server using UDP
 */
//public class UDPClientThread extends AsyncTask<byte[], Void, String> {
//    String ipAddress;
//    int port;
//    String input;
//    BufferedReader reader;
//    OutputStreamWriter out;
//    TextView txtServerResponse;
//    ServerModel serverModel;
//
//    /**
//     * Constructor for UDP thread
//     * @param serverResponse Text view that the client should place server response text into.
//     */
//    public UDPClientThread (TextView serverResponse, ServerModel _serverModel) {
//        txtServerResponse = serverResponse;
//        serverModel = _serverModel;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected String doInBackground(byte[]... messages) {
//        StringBuilder serverResponse = new StringBuilder();
//        try {
//
//            DatagramSocket ds = new DatagramSocket();
//            byte[] data = new byte[1000];
//            data = (messages[0] + "\n").getBytes();
//            DatagramPacket packet = new DatagramPacket (data, data.length, InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
//            ds.send(packet);
//
//            byte buffer[] = new byte[1000];
//
//            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
//            ds.receive(incomingPacket);     // Receive packet from client
//            // Get the string message out of the datagram packet
//            String input = new String(incomingPacket.getData(), "UTF-8");
//            serverResponse.append(input);
//            ds.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return serverResponse.toString();
//    }
//
//    @Override
//    protected void onPostExecute(final String s) {
//        txtServerResponse.setText(txtServerResponse.getText().toString() + "\n" + s);
//    }
//}
public class UDPClientThread extends Thread {

    boolean expectResponse;
    ServerModel serverModel;
    byte[] message;

    public UDPClientThread (byte[] _message, ServerModel _serverModel, boolean _expectResponse) {
        message = _message;
        serverModel = _serverModel;
        expectResponse = _expectResponse;
    }

    public void run () {
        StringBuilder serverResponse = new StringBuilder();
        try {

            DatagramSocket ds = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket (message, message.length, InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
            ds.send(packet);

            if (expectResponse) {
                byte buffer[] = new byte[1000];
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                ds.receive(incomingPacket);     // Receive packet from client

                Decoder decoder = new Decoder(incomingPacket.getData(), incomingPacket.getOffset(), incomingPacket.getLength());

                Message unidentifiedMessage = Message.identifyMessage(decoder);

                if (unidentifiedMessage instanceof PeersAnswerMessage) {
                    PeersAnswerMessage peersAnswerMessage = (PeersAnswerMessage) unidentifiedMessage;
                    ServerResponseEvent.postEventBusMessage(peersAnswerMessage.toString());
                } else if (unidentifiedMessage instanceof ResponseMessage) {
                    ResponseMessage responseMessage = (ResponseMessage) unidentifiedMessage;
                    ServerResponseEvent.postEventBusMessage(responseMessage.toString());
                }
            }

            ds.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}