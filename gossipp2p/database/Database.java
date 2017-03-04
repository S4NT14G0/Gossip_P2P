package gossipp2p.database;
import gossipp2p.messages.GossipMessage;
import gossipp2p.messages.PeerMessage;
import gossipp2p.messages.PeersListMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database
{

	// Create a singleton design pattern for the db
	private static Database instance = null;
	
	// Create field for our connection string
	private final String connectionString = "";
	private static Connection connection = null;

	
	protected Database() {
		// Defeat instantiation
	}
	
	/**
	 * Access the database singleton object
	 * @return Return instance of the Database
	 */
	public static synchronized Database getInstance () {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
	public void initializeDatabase (final String _connectionString) throws SQLException {
		this.connection = DriverManager.getConnection(_connectionString);
	}
		
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
		    			+ "' where id=" + _peerMessage.getId();
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
	
	public boolean isExistingGossipMessage (GossipMessage _gossipMessage) {
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
		    	String insertPeerQuery = "insert into messages values (NULL, '"
		    	+ _gossipMessage.getSha256EncodedMessage() + "', '"
		    	+ _gossipMessage.getMessageDate() + "', '"
		    	+ _gossipMessage.getMessage() + "')";
		    	
		    	statement.executeUpdate(insertPeerQuery);
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
