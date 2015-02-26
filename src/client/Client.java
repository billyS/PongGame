package client;

import common.*;
import static common.Global.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.S_PongModel;

/**
 * Start the client that will display the game for a player
 */
class Client {
	
  private static String role = "TCP";
  //private static String role = "MC";
  private static String gameToView = "0";
  private NetMCReader mcReader =null;
  String clientTime = "";
  private ContactTimeServer time;
  
  public Client(String role){
	 this.role = role;
  }
  /*public static void main( String args[] )
  {
	if ( args.length >= 1 )
      role = args[0];
    if ( args.length >= 2 )
      gameToView = args[1]; 
    ( new Client() ).start();
  }
 */
  /**
   * Start the Client
   */
  public void start()
  {
    DEBUG.set( true );
    DEBUG.trace( "Pong Client" );
    DEBUG.trace( "Role [%s] gameToView [%s]", role, gameToView );
    DEBUG.set( DEBUG_STATE_CLIENT );
    C_PongModel       model = new C_PongModel();
    C_PongView        view  = new C_PongView( role + " " +  gameToView );
    C_PongController  cont  = new C_PongController( model, view );
    
    makeContactWithServer( model, cont );

    model.addObserver( view );       // Add observer to the model
    view.setVisible(true);           // Display Screen
  }

  /**
   * Make contact with the Server who controls the game
   * Players will need to know about the model
   *
   * @param model Of the game
   * @param cont Controller (MVC) of the Game
   */
  public void makeContactWithServer( C_PongModel model,
                                     C_PongController  cont )
  {
    // Also starts the Player task that get the current state
    //  of the game from the server
    C_Player player = null;
    // Socket used to connect to server
    Socket tcpS;
    try
    {
      switch ( role )
      {
        case "TCP" :
          DEBUG.trace( "Client.makeContactWithServer TCP" );
          
          tcpS = new Socket(Global.TCP_SERVER_ADDR, Global.TCP_PORT);
          time = new ContactTimeServer();
          time.start();
          player = new C_Player(model, tcpS);
          cont.setWriter(player.getWriter());
          DEBUG.trace( "before player.start() : tcpSocket: " + tcpS + "player: " + player );
          player.start();
   
          DEBUG.assertTrue( player!=null,
                       "Client.makeContactWithPlayer " +
                       "No Player object created" );
          break;
        case "MC" :
        	
        	 mcReader = new NetMCReader(Global.MC_PORT, Global.MC_ADDR);
        	 player = new C_Player(model, mcReader, gameToView); 
             cont.setWriter(player.getWriter());
             
        	 player.start();
        	 break;
      }
    }
    catch ( Exception err )
    {
      DEBUG.error("Client.makeContactWithServer\n%s",
                  err.getMessage() );
    }
  }
  
}
 

class ContactTimeServer extends Thread {
  private Socket theSocket;                      					// Socket used
  private NetStringReader  theIn;                 				// Input stream
  private NetStringWriter theOut;

  public ContactTimeServer( )
  {
    try {
    	 theSocket = new Socket(TCP_SERVER_ADDR, 50003); 
    	 theOut = new NetTCPWriter(theSocket);
    	 theIn  = new NetTCPReader(theSocket); 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void run()                              					// Execution
  {
	  while ( true )
      {
		try {
				 String message = theIn.get();       // From Server
		         DEBUG.trace( "RECEIVED", message );
		        
		         Date dNow = new Date( );
		         SimpleDateFormat ft = 
		         new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss.SSS");//TODO ask Mike why i needed the clients time of day and if i should be setting the player number here???
		         String clientTime = dNow.toString();
		         
		        
		        theOut.put(clientTime);
		        //DEBUG.trace( "Time request recieved", message );
		         
				
		} catch (Exception err) { }
      }
  }

}