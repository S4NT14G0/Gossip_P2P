package edu.fit.santiago.gossipp2p_client.messages;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Represents a GOSSIP message sent by the client
 * @author sroig2013@my.fit.edu
 *
 */
public class GossipMessage extends Message {

    // sha256 encoded message
    String sha256EncodedMessage;
    // Date of message
    Date messageDate;
    // The message itself
    String message;

    // Formatter for the incoming date information
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS'Z'");

    /**
     * Constructs new GossipMessage.
     * @param _sha256EncodedMessage SHA256 encoding of message.
     * @param _messageDate Date of message.
     * @param _message Contents of message.
     */
    public GossipMessage (String _sha256EncodedMessage, final Date _messageDate, final String _message) {
        this.sha256EncodedMessage = _sha256EncodedMessage;
        this.messageDate = _messageDate;
        this.message = _message;
    }


    /**
     * Constructs new GossipMessage.
     * @param _sha256EncodedMessage SHA256 encoding of message.
     * @param _messageDate Date of message.
     * @param _message Contents of message.
     */
    public GossipMessage (final String _sha256EncodedMessage, final String _messageDate, final String _message) {
        this.sha256EncodedMessage = _sha256EncodedMessage;

        try {
            this.messageDate = sdf.parse(_messageDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.message = _message;
    }

    /**
     * Default constructor
     */
    public GossipMessage() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @return Gossip Message SHA 256 encrypted message.
     */
    public String getSha256EncodedMessage() {
        return this.sha256EncodedMessage;
    }

    /**
     *
     * @return Gossip Message date.
     */
    public Date getMessageDate () {
        return this.messageDate;
    }

    /**
     *
     * @return Gossip Message message.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * String representation of GossipMessage.
     */
    public String toString () {
        String strGossipMsg = "GOSSIP:" + this.sha256EncodedMessage + ":" + sdf.format(this.messageDate) +  ":" + this.getMessage() + "%";
        return strGossipMsg;
    }

    @Override
    public Encoder getEncoder() {
        Encoder e = new Encoder().initSequence();
        e.addToSequence(new Encoder(sha256EncodedMessage));
        e.addToSequence(new Encoder(messageDate.getTime())
        .setASN1Type(Encoder.TAG_GeneralizedTime));
        e.addToSequence(new Encoder(message));

        return e.setASN1Type(Encoder.TAG_SEQUENCE);
    }

    @Override
    public Object decode(Decoder dec) throws ASN1DecoderFail {
        return null;
    }
}
