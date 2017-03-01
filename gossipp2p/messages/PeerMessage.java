
package gossipp2p.messages;

public class PeerMessage extends Message {
	
	int id;
	String peerName;
	int portNumber;
	String ipAddress;
	
	public PeerMessage(String _peerName, int _portNumber, String _ipAddress) {
		peerName = _peerName;
		portNumber = _portNumber;
		ipAddress = _ipAddress;
	}
}
