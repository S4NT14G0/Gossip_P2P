
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
	
	public PeerMessage (String[] _peerMessage) {
		
		// If the ipAddress is seperated using '.'
		if (_peerMessage.length == 4) {
			// Grab the new peer's name
			this.peerName = _peerMessage[1];
			// Split the second token to get the port number
			portNumber = Integer.parseInt (_peerMessage[2].split("=")[1]);
			// Store the ipAddress
			ipAddress = _peerMessage[3];
		}
	}
	
	public PeerMessage(String _peerName, int _portNumber, String _ipAddress) {
		peerName = _peerName;
		portNumber = _portNumber;
		ipAddress = _ipAddress;
	}
}
