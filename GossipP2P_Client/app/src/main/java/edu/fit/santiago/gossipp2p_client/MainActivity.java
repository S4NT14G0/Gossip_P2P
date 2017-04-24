package edu.fit.santiago.gossipp2p_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraCaptureSession;
import android.icu.util.Calendar;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import edu.fit.santiago.gossipp2p_client.database.MessageDaoImpl;
import edu.fit.santiago.gossipp2p_client.database.PeerDaoImpl;
import edu.fit.santiago.gossipp2p_client.events.IncomingServerMessageEvent;
import edu.fit.santiago.gossipp2p_client.models.*;
import edu.fit.santiago.gossipp2p_client.socket_threads.ServerService;

/**
 * Main activity for setting up server connection information
 */
public class MainActivity extends AppCompatActivity {

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }


    TextView serverTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load the main content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Start the peer manager
        PeerManager peerManager = new PeerManager();

        //new PeerDaoImpl(MyApplication.getAppContext()).deleteAll();
        //new MessageDaoImpl(MyApplication.getAppContext()).deleteAll();

        // Start the server service
        Intent intent = new Intent(this, ServerService.class);
        intent.putExtra("IP", wifiIpAddress());
        startService(intent);

        // Get access to the textview
        serverTextView = (TextView) findViewById(R.id.txtServerIncoming);
        serverTextView.setMovementMethod(new ScrollingMovementMethod());

        // Find the gossip button in the view
        Button btnGossip = (Button) findViewById(R.id.btnGossip);

        // Set an onclick listener for gossip click
        btnGossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current values for serve to send to
                ServerModel serverModel = getServerValuesFromUI();

                // UI Validation
                if (serverModel.getConnectionType() != -1
                        && serverModel.getPort() != -1
                        && !serverModel.getIpAddress().isEmpty()) {

                    // Start gossip activity
                    Intent intent = new Intent(MainActivity.this, GossipMessageActivity.class);
                    intent.putExtra("ServerModel", serverModel);
                    startActivity(intent);
                } else {
                    promptMissingFields();
                }
            }
        });

        Button btnAddPeer = (Button)  findViewById(R.id.btnAddPeer);
        btnAddPeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerModel serverModel = getServerValuesFromUI();

                if (serverModel.getConnectionType() != -1
                        && serverModel.getPort() != -1
                        && !serverModel.getIpAddress().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, AddPeerActivity .class);
                    intent.putExtra("ServerModel", serverModel);
                    startActivity(intent);
                } else {
                    promptMissingFields();
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIncomingServerMessageEvent(IncomingServerMessageEvent event) {
        serverTextView.setText(serverTextView.getText() + "\n" + event.message);
    }

    private ServerModel getServerValuesFromUI () {
        EditText etIpAddress = (EditText) findViewById(R.id.txtServer);
        EditText etPort = (EditText) findViewById(R.id.txtPort);
        RadioGroup rbGroup = (RadioGroup) findViewById(R.id.rdGrpConnType);

        int port = -1;

        String ipAddress = etIpAddress.getText().toString();

        if (!etPort.getText().toString().isEmpty())
            port = Integer.parseInt(etPort.getText().toString());

        int connType = rbGroup.getCheckedRadioButtonId();

        return  new ServerModel(ipAddress, port, connType);
    }

    private void promptMissingFields () {
        // Create dialog if fields are missing
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Invalid Values");
        alertDialog.setMessage("Please fill out all fields.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected String wifiIpAddress() {
        Context context = getApplicationContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

}
