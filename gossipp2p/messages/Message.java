// Super class for all accepted messages
// Taking advantage of Java's polymorphism
package gossipp2p.messages;

public class Message {
	
	/**
	 * Identifies message type and returns new instance based on message type.
	 * @param strMessage String message to be identified.
	 * @return Returns a message that has been identified by strMessage.
	 */
	public Message identifyMessage (String strMessage) {
		String[] tokens = strMessage.split(":");
		
		
		
		return new Message();
	}
}
