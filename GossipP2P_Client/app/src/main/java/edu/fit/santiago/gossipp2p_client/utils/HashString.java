package edu.fit.santiago.gossipp2p_client.utils;

import android.util.Base64;
import java.security.MessageDigest;

/**
 * Class to create sha256 hash
 */
public class HashString {

    /**
     * Generates a Sha-256 & base64 encoded hash
     *
     * @param text
     *
     * @return the hash String
     */
    public static String getSHA256HashString(String text){

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(text.getBytes("US-ASCII"));
            return Base64.encodeToString(hash, 0).split("\\n")[0];
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}




