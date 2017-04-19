package edu.fit.santiago.gossipp2p_client.messages;

import java.math.BigInteger;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Created by Santiago on 4/19/2017.
 */

public class LeaveMessage extends Message {

    private String name;

    public LeaveMessage (String _name) {
        name = _name;
    }

    public LeaveMessage() {

    }

    public String toString () {
        return name + " leaving.";
    }

    public void setName (String _name) {
        name = _name;
    }

    public String getName () {
        return name;
    }

    @Override
    public Encoder getEncoder() {
        Encoder e = new Encoder().initSequence();

        e.addToSequence(new Encoder(name)).setASN1Type(Encoder.TAG_UTF8String);

        return e.setExplicitASN1Tag(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, new BigInteger("4"));
    }

    @Override
    public Object decode(Decoder dec) throws ASN1DecoderFail {
        Decoder d = dec.getContent().getContent();

        name = d.getFirstObject(true).getString(Encoder.TAG_UTF8String);

        return this;
    }
}
