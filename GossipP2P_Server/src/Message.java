/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2017 
                Author:  sroig2013@my.fit.edu
                Florida Tech, Computer Science
   
       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.
   
      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.
  
      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */


/**
 * Super class that represents messages received by the server
 * @author Santiago Roig
 *
 */
public class Message {
		
	public static boolean isReadyForParsing (String input) {
		
		if (input.contains("GOSSIP") && input.endsWith("%"))
			return true;
		
		if (input.contains("PEERS?\\n") && input.endsWith("\\n")) 
			return true;
		
		if (input.contains("PEER:") && input.endsWith("%"))
			return true;
		
		return false;
	}
	
	/**
	 * Identifies message type and returns new instance based on message type.
	 * @param strMessage String message to be identified.
	 * @return Returns a message that has been identified by strMessage.
	 */
	public static Message identifyMessage (String strMessage) {
		String[] tokens = strMessage.split(":|%|\\n");
		
		if (tokens[0].equals("GOSSIP")) {
			if (!tokens[1].isEmpty() && !tokens[2].isEmpty() && !tokens[3].isEmpty())
				return new GossipMessage(tokens[1], tokens[2], tokens[3]);
			else
				return new ErrorMessage();
		}
		else if (tokens[0].equals("PEER")) {
			if (!tokens[1].isEmpty() && !tokens[2].isEmpty())
				return new PeerMessage(tokens);
			else
				return new ErrorMessage();
		} else if (tokens[0].equals("PEERS?\\n")) {
			return new PeersListMessage();
		}
		
		return new ErrorMessage();
	}
}
