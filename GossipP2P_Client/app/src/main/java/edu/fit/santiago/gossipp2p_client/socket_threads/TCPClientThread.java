package edu.fit.santiago.gossipp2p_client.socket_threads;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;
import edu.fit.santiago.gossipp2p_client.events.ServerResponseEvent;
import edu.fit.santiago.gossipp2p_client.messages.Message;
import edu.fit.santiago.gossipp2p_client.messages.PeersAnswerMessage;
import edu.fit.santiago.gossipp2p_client.messages.ResponseMessage;
import edu.fit.santiago.gossipp2p_client.models.ServerModel;

/**
 * Created by Santiago on 3/19/2017.
 */

/**
 * Thread for client to connect to server using TCP
 */
public class TCPClientThread extends Thread {

    ServerModel serverModel;
    byte[] message;
    boolean expectResponse;
    InputStream in;
    byte[] byteBuffer = new byte[1024];

    public TCPClientThread (byte[] _message, ServerModel _serverModel, boolean _expectResponse) {
        serverModel = _serverModel;
        message = _message;
        expectResponse = _expectResponse;
    }

    public void run () {
        try {
            Socket socket = new Socket();
            SocketAddress sa = new InetSocketAddress(InetAddress.getByName(serverModel.getIpAddress()), serverModel.getPort());
            socket.connect(sa, 10000);

            OutputStream out = socket.getOutputStream();
            in = socket.getInputStream();

            out.write(message);
            out.flush();

            if (expectResponse) {
                int messageLen = in.read(byteBuffer);
                if (messageLen <= 0) return;

                Decoder decoder = new Decoder(byteBuffer, 0, messageLen);

                if (!decoder.fetchAll(in)) {
                    return;
                }

                Message unidentifiedMessage = Message.identifyMessage(decoder);

                if (unidentifiedMessage instanceof PeersAnswerMessage) {

                    PeersAnswerMessage peersAnswerMessage = (PeersAnswerMessage) unidentifiedMessage;
                    ServerResponseEvent.postEventBusMessage(peersAnswerMessage.toString());
                } else if (unidentifiedMessage instanceof ResponseMessage) {
                    ResponseMessage responseMessage = (ResponseMessage) unidentifiedMessage;
                    ServerResponseEvent.postEventBusMessage(responseMessage.toString());
                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }