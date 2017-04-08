package edu.fit.santiago.gossipp2p_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.fit.santiago.gossipp2p_client.messages.GossipMessage;

/**
 * Created by Santiago on 4/3/2017.
 */

public class MessageDaoImpl extends SQLiteOpenHelper implements MessageDao {

    private static int VERSION = 1;
    private static final String DB_NAME = "MessageDB";
    private static final String TABLE_NAME = "Messages";
    public static final String[] COLUMNS = {"Id", "SHA256EncodedMessage", "Date", "Message"};

    public MessageDaoImpl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
    }

    public MessageDaoImpl (Context context) {
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

    private synchronized void createTables (SQLiteDatabase db) {
        String sqlCreatePeersTable = "CREATE TABLE IF NOT EXISTS peers("
                + COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMNS[1] + " string,"
                + COLUMNS[2] + " string,"
                + COLUMNS[3] + " string);";

        db.execSQL(sqlCreatePeersTable);
    }

    public synchronized void deleteAll() {
        // Delete everything from db and reset primary index
        getWritableDatabase().execSQL("delete from " + TABLE_NAME);
        getWritableDatabase().execSQL("Delete from sqlite_sequence where name='" + TABLE_NAME + "'");
    }

    @Override
    public synchronized void insertGossipMessage(GossipMessage gossipMessage) {
        // Insert Gossip Message
        ContentValues values = new ContentValues();
        values.put(COLUMNS[1], gossipMessage.getSha256EncodedMessage());
        values.put(COLUMNS[2], gossipMessage.getMessageDate().toString());
        values.put(COLUMNS[3], gossipMessage.getMessage());

        getWritableDatabase().insert(TABLE_NAME, null, values);
    }


    @Override
    public synchronized boolean isExistingMessage(GossipMessage gossipMessage) {
        SQLiteDatabase db = getReadableDatabase();

        String selectPeersQuery = "select * from peers where " +
                COLUMNS[1] + " = " + "'" + gossipMessage.getSha256EncodedMessage() + "'"
                + " AND " + COLUMNS[2] + " = " + "'" + gossipMessage.getMessageDate() + "'"
                + " AND " + COLUMNS[3] + " = " + "'" + gossipMessage.getMessage() + "'";

        Cursor cursor = db.rawQuery(selectPeersQuery, null);

        if (cursor != null && cursor.getCount() > 0)
            return true;
        else
            return false;

    }
}
