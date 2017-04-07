package edu.fit.santiago.gossipp2p_client;

import android.app.Application;
import android.content.Context;

/**
 * Created by Santiago on 4/6/2017.
 */

public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
