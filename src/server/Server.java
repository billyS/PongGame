package server;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.Global.*;
import common.*;

/**
 * Start the game server
 *  The call to makeActiveObject() in the model 
 *   starts the play of the game
 */
class Server
{
	private NetStringWriter leftBat,rightBat;
	private NetMCWriter mc = null;
	private ServerSocket serverSocket;
	private ServerSocket ss;
	ExecutorService es;
	//static String role ="TCP";
	static String role ="MC";
	
	

	public static void main( String args[] )
	{
		if(args.length > 0){
			
			if(args[0] == "MC")role = "MC";//TODO check this is correct	
		}
		
		( new Server()   ).start();
	}

	
	public void start() {
		
		DEBUG.set( true );
		DEBUG.trace("Pong Server");
		DEBUG.set( false );
		
		es = Executors.newFixedThreadPool(Global.THREAD_POOL);				// Thread pool to limit number of games. currently 4
		try {
																			// Server socket	
			serverSocket = new ServerSocket(Global.TCP_PORT);
			ss = new ServerSocket(50003);
		}catch( Exception e) {
			DEBUG.trace("Pong Server FAIL" + e.getMessage() );
		}
		
		
		while (true) {													  // Allow the creation of multiple games
		
			S_PongModel model = new S_PongModel(); 						  // Model of game
			
			makeContactWithClients( model, serverSocket );					
			
			S_PongView  view  = new S_PongView(role); 						  // View of game
			
			switch (role) {
			case "MC":
				try {
					mc = new NetMCWriter(Global.MC_PORT, Global.MC_ADDR);
					view.setStreams(mc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				break;

			default:
				view.setStreams(leftBat, rightBat,mc);
				(new TimeServer(model,model.hashCode(), ss)).process();
				break;
			}
			
			new S_PongController( model, view ); 						  // Controller of game
			model.addObserver( view );        							  // Add observer to the model
			model.makeActiveObject(es);          					      // Start play
		}
		
	}

	/**
	 * Make contact with the clients who wish to play
	 * Players will need to know about the model
	 * @param model  Of the game
	 * @param serverSocket Of the game
	 */
	public void makeContactWithClients( S_PongModel model, ServerSocket serverSocket)
	{
		try {
			Socket s1 = serverSocket.accept();	
			NetStringReader reader = new NetTCPReader(s1);
			S_Player player1 = new S_Player(0, model, reader);							// Player 1
			leftBat = new NetTCPWriter(s1);
			//(new TimeServer(model,s1.hashCode(), ss)).process();
			
			Socket s2 = serverSocket.accept();											// Socket for player 2
			NetStringReader reader2 = new NetTCPReader(s2);
			S_Player player2 = new S_Player(1, model, reader2);							// Player 2
			rightBat = new NetTCPWriter(s2);
			
			
			es.submit(player1);															// Start player 1
			es.submit(player2);															// Start player 2
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}