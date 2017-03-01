import java.util.Date;

public class GossipMessage {
	
	int id;
	String sha256EncodedMessage;
	Date messageDate;
	String message;
	
	public GossipMessage (String _sha256EncodedMessage, final Date _messageDate, final String _message) {
		this.sha256EncodedMessage = _sha256EncodedMessage;
		this.messageDate = _messageDate;
		this.message = _message;
	}	
}
