package edu.fit.santiago.gossipp2p_client.messages;

import java.math.BigInteger;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Created by Santiago on 4/5/2017.
 */

public class PeersQueryMessage extends Message {

    @Override
    public Encoder getEncoder() {
        Encoder e = new Encoder().initSequence();
        e.addToSequence(new Encoder().getNullEncoder());


        return e.setASN1Type(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, new BigInteger("3"));
    }

    @Override
    public Object decode(Decoder dec) throws ASN1DecoderFail {
//        PeersQueryMessage peersQueryMessage = new PeersQueryMessage();
//
        Decoder d = dec.getContent();
//        peersQueryMessage = d.getAny();
//        peersAnswerMessage.peers = d.getSequenceOfAL(Encoder.TAG_SEQUENCE, new PeerMessage());
//        peerMessage.peerName = d.getFirstObject(true).getString(Encoder.TAG_UTF8String);
//        peerMessage.portNumber = d.getFirstObject(true).getInteger(Encoder.TAG_INTEGER).intValue();
//        peerMessage.ipAddress = d.getFirstObject(true).getString(Encoder.TAG_PrintableString);
        return new PeersQueryMessage();
    }
}
