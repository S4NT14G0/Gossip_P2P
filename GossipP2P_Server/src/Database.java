/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2017 
                Author:  sroig2013@my.fit.edu
                Florida Tech, Computer Science
   
       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.
   
      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.
  
      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.File;

/**
 * Database class for Gossip P2P Server
 * @author sroig2013@my.fit.edu
 *
 */
public class Database
{
	// Create a singleton design pattern for the db
	private static Database instance = null;
	
	// Create field for our connection string
	private String connectionString = "";
	private static Connection connection = null;
	private final String connectionStringHeader = "jdbc:sqlite:";

	protected Database() {
		// Defeat instantiation
	}
	
	/**
	 * Access the database singleton object.
	 * @return Return instance of the Database.
	 */
	public static synchronized Database getInstance () {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	/**
	 * Setup database using the path passed in from args.
	 * @param _pathToDB Path to the database.
	 * @throws SQLException
	 */
	public void initializeDatabase (final String _pathToDB) throws SQLException {
		File file = new File (_pathToDB);

		if (file.exists()) {
			this.connectionString = connectionStringHeader + _pathToDB;
			this.connection = DriverManager.getConnection(connectionString);	
		} else {
			this.connectionString = connectionStringHeader + _pathToDB;
			this.connection = DriverManager.getConnection(connectionString);
			createTables();
		}
	}

	private void createTables () throws SQLException {
		    String sqlCreateMessageTable = "CREATE TABLE IF NOT EXISTS messages("
            + "  id           INTEGER PRIMARY KEY,"
            + "   sha256EncodedMessage            string,"
            + "   date          string,"
            + "   message           string);";

			String sqlCreatePeersTable = "CREATE TABLE IF NOT EXISTS peers("
            + "  id           INTEGER PRIMARY KEY,"
            + "   name            string,"
            + "   port          integer,"
            + "   ipAddress           string);";

    	Statement stmt = connection.createStatement();
    	stmt.execute(sqlCreateMessageTable);
		stmt.execute(sqlCreatePeersTable);
	}
	
	/**
	 * Checks db for peer message.  Inserts if message doesn't exist. Updates record if it does exist.
	 * @param _peerMessage The message to update or insert into db.
	 * @return Returns true if query is successful.  False if not.
	 */
	public boolean handlePeerMessage (PeerMessage _peerMessage) {
		try {
			// create a database connection
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			// Query for this message to see if it's there
	     	ResultSet rs = statement.executeQuery("select * from peers where " +
	     			"name=" + "'" + _peerMessage.getPeerName() + "'");
		    
		    if(rs.next())
		    {
		    	// Record exists so we need to update it
		    	String updatePeerQuery = "update peers set port=" + _peerMessage.getPortNumber()
		    			+ ", ipAddress='" + _peerMessage.getIpAddress()
		    			+ "' where id=" + rs.getInt("id");
		    	statement.executeUpdate(updatePeerQuery);
		    } else {
		    	//Record doesn't exist so we need to insert a new one
		    	String insertPeerQuery = "insert into peers values (NULL, '"
		    	+ _peerMessage.getPeerName() + "', "
		    	+ _peerMessage.getPortNumber() + ", '"
		    	+ _peerMessage.getIpAddress() + "')";
		    	
		    	statement.executeUpdate(insertPeerQuery);
		    }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Exception our query failed
			return false;
		}
		
		// Made it to end pass
		return true;
	}
	
	/**
	 * Checks db for Gossip Message.  Inserts if message doesn't exist.  Updates record if it does exist.
	 * @param _gossipMessage Message to search for in db.
	 * @return
	 */
	public boolean handleGossipMessage (GossipMessage _gossipMessage) {
		try {
			// create a database connection
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			// Query for this message to see if it's there
	     	ResultSet rs = statement.executeQuery("select * from messages where " +
	     			"sha256EncodedMessage='" + _gossipMessage.getSha256EncodedMessage() + "'"
	     			+"and message='" + _gossipMessage.getMessage() + "'");
		    
		    if(!rs.next())
		    {
		    	//Record doesn't exist so we need to insert a new one
		    	String insertGossipQuery = "insert into messages values (NULL, '"
		    	+ _gossipMessage.getSha256EncodedMessage() + "', '"
		    	+ _gossipMessage.getMessageDate() + "', '"
		    	+ _gossipMessage.getMessage() + "')";
		    	
		    	statement.executeUpdate(insertGossipQuery);
		    	return false;
		    } else {
		    	return true;
		    }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Exception our query failed
			return false;
		}
	}
	
	/**
	 * Queries list of all known peers in the db.
	 * @return List of all known peers.
	 */
	public PeersListMessage getPeersList () {
		try {
			
			Statement statement = connection.createStatement();
			String queryPeerList = "select * from peers";
			ResultSet rs = statement.executeQuery(queryPeerList);
			
			ArrayList<PeerMessage> peers = new ArrayList<PeerMessage>();
			
		    while(rs.next())
		    {
		    	// Create a peer from db records
		    	PeerMessage peer = new PeerMessage(rs.getString("name"), 
		    			rs.getInt("port"),
		    			rs.getString("ipAddress"));
		    	// Add peer to list of peers
		    	peers.add(peer);
		    }
		    
		    return new PeersListMessage(peers);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Failure
			return null;
		}
	}
	
	
}
