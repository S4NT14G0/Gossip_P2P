package edu.fit.santiago.gossipp2p_client.messages;/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2017 
                Author:  sroig2013@my.fit.edu
                Florida Tech, Computer Science
   
       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.
   
      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.
  
      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */

import java.math.BigInteger;
import java.util.ArrayList;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Represents list of known peers.
 * @author sroig2013@my.fit.edu
 *
 */
public class PeersAnswerMessage extends Message {
	ArrayList<PeerMessage> peers;
	
	/**
	 * Constructs new list of peers.
	 * @param _peers List of known peers.
	 */
	public PeersAnswerMessage(ArrayList<PeerMessage> _peers) {
		this.peers = _peers;
	}

	public PeersAnswerMessage() {
		// TODO Auto-generated constructor stub
		peers = new ArrayList<PeerMessage>();
	}
	
	/**
	 * 
	 * @return List of known peers.
	 */
	public ArrayList<PeerMessage> getPeers () {
		return this.peers;
	}
	
	/**
	 * String representation of Peers List.
	 */
	public String toString () {
		if (this.peers.size() > 0) {
			String peersList = "PEERS|" + this.peers.size() + "|";
			
			for (PeerMessage peer : this.peers) {
				peersList += peer.peerName + ":"
						+ "PORT=" + peer.portNumber
						+ "IP=" + peer.ipAddress + "|";
			}
			
			peersList += "%";
			
			return peersList;
		} else {
			return "PEERS|0|";
		}
	}

    @Override
    public Encoder getEncoder() {
		Encoder e = new Encoder().initSequence();

		e.addToSequence(Encoder.getEncoder(peers)).setASN1Type(Encoder.TAG_SEQUENCE);

		return e.setExplicitASN1Tag(Encoder.CLASS_CONTEXT, Encoder.PC_CONSTRUCTED, new BigInteger("1"));
    }

    @Override
    public Object decode(Decoder dec) throws ASN1DecoderFail {
        PeersAnswerMessage peersAnswerMessage = new PeersAnswerMessage();

        Decoder d = dec.getContent();

        peersAnswerMessage.peers = d.getFirstObject(false).getSequenceOfAL(Encoder.TAG_SEQUENCE, new PeerMessage());

        return peersAnswerMessage;
    }
}
