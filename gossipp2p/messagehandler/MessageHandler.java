package gossipp2p.messagehandler;

import gossipp2p.messages.GossipMessage;
import gossipp2p.messages.Message;
import gossipp2p.messages.PeerMessage;

public class MessageHandler {

	public static void HandleGossipMessage(GossipMessage gossipMessage) {
		// 1. Check DB to see if message is already known
		// 2. If known discard message and print DISCARDED on standard error
		// 3. If unknown store it, broadcast it to all known peers, and then
		// print it on standard error.
	}
	
	public static void HandlePeerMessage (PeerMessage peerMessage) {
		// 1. Check if DB knows this peer
		// 2. If it doesn't then store the peer
		// 3. If it does then update the address
	}
	
	public static void HandlePeersListMessage (int port, String ipAddress) {
		// Build up PeersListMessage from database
		// Respond to the client with the PeersListMessage
	}
	
}
