// Super class for all accepted messages
// Taking advantage of Java's polymorphism
package gossipp2p.messages;

/**
 * Super class that represents messages received by the server
 * @author Santiago Roig
 *
 */
public class Message {
		
	/**
	 * Identifies message type and returns new instance based on message type.
	 * @param strMessage String message to be identified.
	 * @return Returns a message that has been identified by strMessage.
	 */
	public static Message identifyMessage (String strMessage) {
		String[] tokens = strMessage.split(":|%");
		
		if (tokens[0].equals("GOSSIP")) {
			return new GossipMessage(tokens[1], tokens[2], tokens[3]);			
		}
		else if (tokens[0].equals("PEER")) {
			
		}
		
		return new ErrorMessage();
	}
}
