
package gossipp2p.messages;

/**
 * Message that will add a new peer to the list of known peers
 * @author santi
 *
 */
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
