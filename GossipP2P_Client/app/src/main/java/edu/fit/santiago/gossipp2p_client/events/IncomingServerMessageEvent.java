package edu.fit.santiago.gossipp2p_client.events;

/**
 * Created by Santiago on 4/4/2017.
 */

public class IncomingServerMessageEvent {
    public final String message;

    public IncomingServerMessageEvent (String message) {
        this.message = message;
    }
}
