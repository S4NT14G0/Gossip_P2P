/* ------------------------------------------------------------------------- */
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


package edu.fit.santiago.gossipp2p_client.messages;
/**
 * Message that will add a new peer to the list of known peers
 * @author sroig2013@my.fit.edu
 *
 */
public class PeerMessage {

    String peerName = "";
    int portNumber = -1;
    String ipAddress = "";

    /**
     * Constructs a new peer message.
     *
     * @param _peerName
     * @param _portNumber
     * @param _ipAddress
     */
    public PeerMessage(String _peerName, int _portNumber, String _ipAddress) {
        peerName = _peerName;
        portNumber = _portNumber;
        ipAddress = _ipAddress;
    }

    /**
     * Default constructor
     */
    public PeerMessage() {
    }

    public String toString() {
        return "PEER:" + this.peerName + ":PORT=" + this.portNumber + ":IP=" + this.ipAddress + "%";
    }


    /**
     * @return Peer Message peer name.
     */
    public String getPeerName() {
        return this.peerName;
    }

    /**
     * @return Peer Message port number.
     */
    public int getPortNumber() {
        return this.portNumber;
    }

    /**
     * @return Peer Message IP Address.
     */
    public String getIpAddress() {
        return this.ipAddress;
    }
}