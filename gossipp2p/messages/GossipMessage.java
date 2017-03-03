package gossipp2p.messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Represents a GOSSIP message received by the server
 * @author Santiago Roig
 *
 */
public class GossipMessage extends Message {
	
	// Unique ID of message
	int id;
	// sha256 encoded message
	String sha256EncodedMessage;
	// Date of message
	Date messageDate;
	// The message itself
	String message;
	
	// Formatter for the incoming date information
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS'Z'");
		
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
	
	public GossipMessage (String _sha256EncodedMessage, final String _messageDate, final String _message) {
		this.sha256EncodedMessage = _sha256EncodedMessage;

		try {
			this.messageDate = sdf.parse(_messageDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.message = _message;
	}
	
	public GossipMessage (String _sha256EncodedMessage, final String _messageDate, final String _message, final int _id) {
		this.sha256EncodedMessage = _sha256EncodedMessage;
		try {
			this.messageDate = sdf.parse(_messageDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		this.message = _message;
		this.id = _id;
	}	

	public GossipMessage() {
		// TODO Auto-generated constructor stub
	}	
}
