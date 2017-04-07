package edu.fit.santiago.gossipp2p_client.events;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Santiago on 4/6/2017.
 */

public class ServerResponseEvent {
    public final String message;

    public ServerResponseEvent (String message) {
        this.message = message;
    }

    public static void postEventBusMessage (String message) {
        EventBus.getDefault().post(new ServerResponseEvent(message));
    }
}
