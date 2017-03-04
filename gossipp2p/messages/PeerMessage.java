
package gossipp2p.messages;

/**
 * Message that will add a new peer to the list of known peers
 * @author santi
 *
 */
public class PeerMessage extends Message {
	
	int id = -1;
	String peerName = "";
	int portNumber = -1;
	String ipAddress = "";
	
	public PeerMessage (String[] _peerMessage) {
		
		// If the ipAddress is seperated using '.'
		if (_peerMessage.length == 4) {
			// Handle IPV4 IP Address
			// Grab the new peer's name
			this.peerName = _peerMessage[1];
			// Split the second token to get the port number
			portNumber = Integer.parseInt (_peerMessage[2].split("=")[1]);
			// Store the ipAddress
			ipAddress = _peerMessage[3].split("=")[1];			
		} else if (_peerMessage.length == 7) {
			// Handle IPV6 IP Address
			// Grab the new peer's name
			this.peerName = _peerMessage[1];
			// Split the second token to get the port number
			portNumber = Integer.parseInt (_peerMessage[2].split("=")[1]);
			// Store the ipAddress
			ipAddress = _peerMessage[3].split("=")[1]
					+ ":" + _peerMessage[4]
					+ ":" + _peerMessage[5]
					+ ":" + _peerMessage[6];			
		}
	}
	
	public PeerMessage(String _peerName, int _portNumber, String _ipAddress) {
		peerName = _peerName;
		portNumber = _portNumber;
		ipAddress = _ipAddress;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getPeerName () {
		return this.peerName;
	}
	
	public int getPortNumber () {
		return this.portNumber;
	}
	
	public String getIpAddress () {
		return this.ipAddress;
	}
}
