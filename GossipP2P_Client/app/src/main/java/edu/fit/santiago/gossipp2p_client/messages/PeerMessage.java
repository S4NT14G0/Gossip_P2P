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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Message that will add a new peer to the list of known peers
 * @author sroig2013@my.fit.edu
 *
 */
public class PeerMessage extends Message{

    String peerName = "";
    int portNumber = -1;
    String ipAddress = "";
    String dateOfLastContact = "";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS'Z'");

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

    public PeerMessage () {}

    /**
     * Constructs a new peer message.
     *
     * @param _peerName
     * @param _portNumber
     * @param _ipAddress
     * @param _dateOfLastContact
     */
    public PeerMessage(String _peerName, int _portNumber, String _ipAddress, Date _dateOfLastContact) {
        peerName = _peerName;
        portNumber = _portNumber;
        ipAddress = _ipAddress;
        dateOfLastContact = sdf.format(_dateOfLastContact);
    }

    /**
     * Constructs a new peer message.
     *
     * @param _peerName
     * @param _portNumber
     * @param _ipAddress
     * @param _dateOfLastContact
     */
    public PeerMessage(String _peerName, int _portNumber, String _ipAddress, String _dateOfLastContact) {
        peerName = _peerName;
        portNumber = _portNumber;
        ipAddress = _ipAddress;
        dateOfLastContact = _dateOfLastContact;
    }

    /**
     * Constructs new Peer Message.
     * @param _peerMessage Array of strings to be turned into peer message.
     */
    public PeerMessage (String[] _peerMessage) {

        // If the ipAddress is seperated using '.'
        if (_peerMessage.length == 4) {
            // Handle IPV4 IP Address
            // Grab the new peer's name
            this.peerName = _peerMessage[1];
            // Split the second token to get the port number
            portNumber = Integer.parseInt (_peerMessage[2].split("=")[1]);
            // Store the ipAddress
            ipAddress = _peerMessage[3].split("=")[1];
        } else if (_peerMessage.length == 7) {
            // Handle IPV6 IP Address
            // Grab the new peer's name
            this.peerName = _peerMessage[1];
            // Split the second token to get the port number
            portNumber = Integer.parseInt (_peerMessage[2].split("=")[1]);
            // Store the ipAddress
            ipAddress = _peerMessage[3].split("=")[1]
                    + ":" + _peerMessage[4]
                    + ":" + _peerMessage[5]
                    + ":" + _peerMessage[6];
        }
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

    public String getDateOfLastContact () {return this.dateOfLastContact;}
    public static byte getType() {
        return Encoder.buildASN1byteType(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, (byte)2);
    }
    @Override
    public Encoder getEncoder() {
        Encoder e = new Encoder().initSequence();

        e.addToSequence(new Encoder(peerName))
                .setASN1Type(Encoder.TAG_UTF8String);

        e.addToSequence(new Encoder(portNumber)
                .setASN1Type(Encoder.TAG_INTEGER));

        e.addToSequence(new Encoder(ipAddress))
                .setASN1Type(Encoder.TAG_PrintableString);

        return e.setASN1Type(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, new BigInteger("2"));
    }

    @Override
    public PeerMessage decode(Decoder dec) throws ASN1DecoderFail {

        Decoder d = dec.getContent();

        peerName = d.getFirstObject(true).getString();
        portNumber = d.getFirstObject(true).getInteger().intValue();
        ipAddress = d.getFirstObject(true).getString();

        return this;
    }
    public PeerMessage instance() {
        return new PeerMessage();
    }
}