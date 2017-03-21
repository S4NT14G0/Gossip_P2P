package edu.fit.santiago.gossipp2p_client;

import java.util.Base64;
import java.security.MessageDigest;

public class HashString {

    /**
     * Generates a Sha-256 & base64 encoded hash
     *
     * @param text
     *
     * @return the hash String
     */
    public String HashString(String text){

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes("US-ASCII"));
            return Base64.getEncoder().encodeToString(md.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}




