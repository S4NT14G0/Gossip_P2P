package edu.fit.santiago.gossipp2p_client.models;

import java.util.Calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import edu.fit.santiago.gossipp2p_client.MyApplication;
import edu.fit.santiago.gossipp2p_client.database.PeerDaoImpl;
import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;
import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;

/**
 * Created by Santiago on 4/22/2017.
 */

public class PeerManager {
    public PeerManager () {
        PeerDaoImpl peerDao = new PeerDaoImpl(MyApplication.getAppContext());
        ArrayList<PeerMessage> peers = peerDao.getAllPeers().getPeers();

        for (PeerMessage peer : peers) {
            AutomatedPeerRemoval peerRemoval = new AutomatedPeerRemoval(peer);
            peerRemoval.start();
        }
    }

    public static void startPeerTimeout(PeerMessage peerMessage) {
        AutomatedPeerRemoval peerRemoval = new AutomatedPeerRemoval(peerMessage);
        peerRemoval.start();
    }
}


class AutomatedPeerRemoval extends Thread {
    private  PeerMessage peer;

    public AutomatedPeerRemoval (PeerMessage _peer) {
        peer = _peer;
    }

    public void run () {

        for (;;) {
            // First check if the user has already surpassed their stay
            try {
                PeerDaoImpl peerDao = new PeerDaoImpl(MyApplication.getAppContext());
                peer = peerDao.getPeerByName(peer.getPeerName());

                if (peer == null)
                    return;

                Calendar peerContact = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();

                Date peersLastContact = peer.getDateTypeOfLastContact();
                Date currentDandT = new Date();

                peerContact.setTime(peersLastContact);
                currentDate.setTime(currentDandT);

                int daysSinceContact = daysBetween(currentDate.getTime(), peerContact.getTime());
                long secondsSecondSinceContact = diffSeconds(currentDate.getTime(), peerContact.getTime());

                if (daysSinceContact >= 2) {
                    peerDao.deletePeer(peer.getPeerName());
                    IncomingServerMessageEvent.postEventBusMessage("Removed Peer " + peer.getPeerName() + " due to inactivity");
                     return;
                }
                else {
                    // Calculate the time they have remaining until they get booted and put thread to sleep
                    peerContact.add(Calendar.DATE, 2);
                    //currentDate.add(Calendar.SECOND, 30);
                    Long milliseconds = currentDate.getTime().getTime() - peerContact.getTime().getTime();
                    Thread.sleep(milliseconds);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public long diffSeconds (Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return diff / 1000;
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}