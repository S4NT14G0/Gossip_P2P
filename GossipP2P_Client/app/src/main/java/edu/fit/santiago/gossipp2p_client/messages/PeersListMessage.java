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

import java.util.ArrayList;

/**
 * Represents list of known peers.
 * @author sroig2013@my.fit.edu
 *
 */
public class PeersListMessage extends Message {
	ArrayList<PeerMessage> peers;
	
	/**
	 * Constructs new list of peers.
	 * @param _peers List of known peers.
	 */
	public PeersListMessage (ArrayList<PeerMessage> _peers) {
		this.peers = _peers;
	}

	public PeersListMessage() {
		// TODO Auto-generated constructor stub
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
}
