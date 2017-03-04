package gossipp2p.messagehandler;

import gossipp2p.database.Database;
import gossipp2p.messages.GossipMessage;
import gossipp2p.messages.Message;
import gossipp2p.messages.PeerMessage;
import gossipp2p.messages.PeersListMessage;

public class MessageHandler {

	public static void HandleGossipMessage(Message _gossipMessage) {
		// 1. Check DB to see if message is already known
		if (Database.getInstance().isExistingGossipMessage((GossipMessage)_gossipMessage)) {
			// 2. If known discard message and print DISCARDED on standard error
			System.err.println("DISCARDED");
		} else {
			// Get list of all peers
			PeersListMessage peersList = Database.getInstance().getPeersList();
			// Broadcast the gossipMessage to everyone on peers list
			
			// Print the message on the standard error
		}
	}
	
	public static void HandlePeerMessage (Message _peerMessage) {
		// 1. Check if DB knows this peer
		// 2. If it doesn't then store the peer
		// 3. If it does then update the address
		Database.getInstance().handlePeerMessage((PeerMessage) _peerMessage);
	}
	
	public static void HandlePeersListMessage (int _port, String _ipAddress) {
		// Build up PeersListMessage from database
		PeersListMessage peersList = Database.getInstance().getPeersList();
		// Respond to the client with the PeersListMessage
	}
	
	
	
}
