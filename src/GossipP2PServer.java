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

import java.net.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Gossip P2P Server
 * @author sroig2013@my.fit.edu
 *
 */
public class GossipP2PServer {

    public static void main (String args[]) {
    	// Arguments that should be passed in
    	int port = -1;
    	String databasePath = "";
    	
    	// Set up arg options
    	Options options = new Options();
    	Option p = new Option("p", true, "Port for server to listen on.");
    	options.addOption(p);
    	Option d = new Option("d", true, "Path to database.");
    	options.addOption(d);
    	
    	CommandLineParser clp = new DefaultParser();
    	
    	try {
    		CommandLine cl = clp.parse(options, args);
    		
    		if (cl.hasOption("p")) {
    			port = Integer.parseInt(cl.getOptionValue("p"));
    		}
    		if (cl.hasOption("d")) {
    			databasePath = cl.getOptionValue("d");
    		}
    		
    		// If we have all we need start the server and setup database.
        	if (port != -1 && !databasePath.isEmpty() && databasePath != null) {
    			Database.getInstance().initializeDatabase(databasePath);
    	    	runIterativeServer(port);
        	} else {
        		showArgMenu(options);
        	}
    		
    	} catch (Exception e) {
			e.printStackTrace();
    	}    	    	    	
    }
    
    /**
     * Show help menu if incorrect arguments are supplied.
     * @param options List of expected options.
     */
    private static void showArgMenu (Options options) {
    	HelpFormatter helpFormatter = new HelpFormatter();
    	helpFormatter.printHelp("Gossip P2P Server", options, true);
    }
    
    /**
     * Setup current server as a concurrent server.
     * @param port Port that server is listening on.
     */
    static void runConcurrentServer(int port) {
    	Socket sock;
    	
    	try {
    		ServerSocket serverSocket = new ServerSocket(port);
    		DatagramSocket dgSocket = new DatagramSocket(port);
    		
    		for (;;) {
    			
				new ConcurrentUDPServerThread(dgSocket).start();
				sock = serverSocket.accept();
				new ConcurrentTCPServerThread(sock).start();
    			
    		}
    	} catch (Exception e) {
    		
    	}    	
    }

    /**
     * Setup current server as iterative server.
     * @param port Port that server is listening on.
     */
    static void runIterativeServer(int port) {
    	
    	try {
			new IterativeTCPServerThread (port).start();
			new IterativeUDPServerThread (port).start();

    	} catch (Exception e) {
    		
    	}    	
    }
    
    

}



