package edu.fit.santiago.gossipp2p_client.socket_threads;

import org.greenrobot.eventbus.EventBus;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;

/**
 * Created by Santiago on 4/4/2017.
 */

public class UDPServerThread extends Thread {
    private static final int MAXBUFFER = 1000;
    DatagramSocket dgSocket;
    DatagramPacket packet;
    byte[] byteBuffer = new byte[MAXBUFFER];

    public UDPServerThread (DatagramSocket _dgSocket) {
        dgSocket = _dgSocket;
    }

    public void run () {
        try{
            String input ="";

            for (;;) {
                packet = new DatagramPacket(byteBuffer, byteBuffer.length);
                dgSocket.receive(packet);
                input = new String(packet.getData(), "UTF-8");
                postEventBusMessage(input);
                byteBuffer = (input + "\n").getBytes();
                packet = new DatagramPacket(byteBuffer, byteBuffer.length, packet.getAddress(), packet.getPort());
                dgSocket.send(packet);
                packet.setLength(byteBuffer.length);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void postEventBusMessage (String message) {
        EventBus.getDefault().post(new IncomingServerMessageEvent(message));
    }
}
