package gossipp2p.messages;

import java.util.ArrayList;

public class PeersListMessage extends Message {
	ArrayList<PeerMessage> peers;
	
	public PeersListMessage (ArrayList<PeerMessage> _peers) {
		this.peers = _peers;
	}

	public PeersListMessage() {
		// TODO Auto-generated constructor stub
	}
}
