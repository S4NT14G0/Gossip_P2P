package edu.fit.santiago.gossipp2p_client;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import edu.fit.santiago.gossipp2p_client.models.*;

/**
 * Main activity for setting up server connection information
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnGossip = (Button) findViewById(R.id.btnGossip);

        btnGossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServerValues ();

                if (ServerModel.getInstance().getConnectionType() != -1
                        && ServerModel.getInstance().getPort() != -1
                        && !ServerModel.getInstance().getIpAddress().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, GossipMessageActivity.class);
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
                setServerValues ();

                if (ServerModel.getInstance().getConnectionType() != -1
                        && ServerModel.getInstance().getPort() != -1
                        && !ServerModel.getInstance().getIpAddress().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, AddPeerActivity .class);
                    startActivity(intent);
                } else {
                    promptMissingFields();
                }

            }
        });
    }

    private void setServerValues () {
        EditText etIpAddress = (EditText) findViewById(R.id.txtServer);
        EditText etPort = (EditText) findViewById(R.id.txtPort);
        RadioGroup rbGroup = (RadioGroup) findViewById(R.id.rdGrpConnType);

        int port = -1;

        String ipAddress = etIpAddress.getText().toString();

        if (!etPort.getText().toString().isEmpty())
            port = Integer.parseInt(etPort.getText().toString());


        int connType = rbGroup.getCheckedRadioButtonId();

        ServerModel.getInstance().setServer(ipAddress, port, connType);

    }

    private void promptMissingFields () {
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
}
