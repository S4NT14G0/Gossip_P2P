package edu.fit.santiago.gossipp2p_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import edu.fit.santiago.gossipp2p_client.messages.PeerMessage;
import edu.fit.santiago.gossipp2p_client.messages.PeersAnswerMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public class PeerDaoImpl extends SQLiteOpenHelper implements PeerDao {

    private static int VERSION = 1;
    private static final String DB_NAME = "PeersDB";
    private static final String TABLE_NAME = "Peers";
    public static final String[] COLUMNS = {"Id", "Name", "Port", "IpAddress", "DateOfLastContact"};

    public PeerDaoImpl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
    }

    public PeerDaoImpl (Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        VERSION = newVersion;
        Log.d("Database - onUpgrade", "OK");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void createTables (SQLiteDatabase db) {
        String sqlCreatePeersTable = "CREATE TABLE IF NOT EXISTS peers("
                + COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMNS[1] + " string,"
                + COLUMNS[2] + " integer,"
                + COLUMNS[3] + " string,"
                + COLUMNS[4] + " string);";

        db.execSQL(sqlCreatePeersTable);
    }

    @Override
    public void updatePeerMessage(PeerMessage peerMessage) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectPeersQuery = "select * from peers where " +
                COLUMNS[1] + " = " + "'" + peerMessage.getPeerName() + "'";

        Cursor cursor = db.rawQuery(selectPeersQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            // Update peer
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS'Z'");
            String currentDateandTime = sdf.format(new Date());

            // Insert peer
            ContentValues values = new ContentValues();
            values.put(COLUMNS[1], peerMessage.getPeerName());
            values.put(COLUMNS[2], peerMessage.getPortNumber());
            values.put(COLUMNS[3], peerMessage.getIpAddress());
            values.put(COLUMNS[4], peerMessage.getDateOfLastContact());

            getWritableDatabase().update(TABLE_NAME, values, COLUMNS[1] + " = " + "'" + peerMessage.getPeerName() + "'", null);
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS'Z'");
            String currentDateandTime = sdf.format(new Date());

            // Insert peer
            ContentValues values = new ContentValues();
            values.put(COLUMNS[1], peerMessage.getPeerName());
            values.put(COLUMNS[2], peerMessage.getPortNumber());
            values.put(COLUMNS[3], peerMessage.getIpAddress());
            values.put(COLUMNS[4], currentDateandTime);

            getWritableDatabase().insert(TABLE_NAME, null, values);
        }
    }

    @Override
    public void deletePeer(String peerName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMNS[1] + " =" + peerName, null);
    }

    @Override
    public PeersAnswerMessage getAllPeers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String getAllPeersQuery = "Select * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(getAllPeersQuery, null);

        ArrayList<PeerMessage> alPeersList = new ArrayList<PeerMessage>();

        while (cursor.moveToNext() && cursor != null)  {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMNS[0]));
            String name = cursor.getString(cursor.getColumnIndex(COLUMNS[1]));
            int port = cursor.getInt(cursor.getColumnIndex(COLUMNS[2]));
            String ipAddress = cursor.getString(cursor.getColumnIndex(COLUMNS[3]));
            String date = cursor.getString(cursor.getColumnIndex(COLUMNS[4]));

            alPeersList.add(new PeerMessage(name, port, ipAddress, date));
        }

        return new PeersAnswerMessage(alPeersList);
    }

    public void deleteAll() {
        // Delete everything from db and reset primary index
        getWritableDatabase().execSQL("delete from " + TABLE_NAME);
        getWritableDatabase().execSQL("Delete from sqlite_sequence where name='" + TABLE_NAME + "'");
    }

    private int indexOfColumn(String name) {

        for (int i = 0; i < COLUMNS.length; ++i) {
            if (COLUMNS[i].equals(name)) {
                return i;
            }
        }
        return 0;
    }
}
