package edu.fit.santiago.gossipp2p_client.messages;

import java.math.BigInteger;

import edu.fit.santiago.gossipp2p_client.asn1.ASN1DecoderFail;
import edu.fit.santiago.gossipp2p_client.asn1.Decoder;
import edu.fit.santiago.gossipp2p_client.asn1.Encoder;

/**
 * Created by Santiago on 4/6/2017.
 */

public class ResponseMessage extends  Message {
    private String response;

    public ResponseMessage (String  _response) {
        this.response = _response;
    }

    public ResponseMessage () {}

    @Override
    public Encoder getEncoder() {
        Encoder e = new Encoder().initSequence();

        e.addToSequence(new Encoder(response))
                .setASN1Type(Encoder.TAG_UTF8String);


        return e.setASN1Type(Encoder.CLASS_APPLICATION, Encoder.PC_CONSTRUCTED, new BigInteger("4"));
    }

    @Override
    public String toString () {
        return this.response;
    }

    @Override
    public Object decode(Decoder dec) throws ASN1DecoderFail {
        ResponseMessage responseMessage = new ResponseMessage();

        Decoder d = dec.getContent();

        responseMessage.response = d.getFirstObject(true).getString();


        return responseMessage;
    }
}
