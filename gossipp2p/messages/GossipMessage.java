package gossipp2p.messages;

import java.sql.Date;

public class GossipMessage extends Message {
	
	int id;
	String sha256EncodedMessage;
	Date messageDate;
	String message;
	
	public GossipMessage (String _sha256EncodedMessage, final Date _messageDate, final String _message) {
		this.sha256EncodedMessage = _sha256EncodedMessage;
		this.messageDate = _messageDate;
		this.message = _message;
	}
	
	public GossipMessage (String _sha256EncodedMessage, final Date _messageDate, final String _message, final int _id) {
		this.sha256EncodedMessage = _sha256EncodedMessage;
		this.messageDate = _messageDate;
		this.message = _message;
		this.id = _id;
	}		
}
