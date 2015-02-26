package client;

import common.*;

import java.io.IOException;
import java.net.Socket;

/**
 * Individual player run as a separate thread to allow
 * updates immediately the bat is moved
 */
class C_Player extends Thread
{
  private Socket          theSocket;            // Socket used
  private NetStringReader theIn  = null;        // Input stream
  private NetStringWriter theOut = null;        // Output stream
  private C_PongModel     model;
  private int gameToView = 0;                   // 1 or more
  private String score[];


  /**
   * Constructor
   * @param model - model of the game
   * @param s - Socket used to communicate with server
   */
  public C_Player( C_PongModel model, Socket s ) {
	  
    // The player needs to know this to be able to work
    this.model      = model;
    theSocket       = s;                     // Remember socket
    try {
    	theOut = new NetTCPWriter(theSocket);
		theIn = new NetTCPReader(theSocket);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  /**
   * Constructor for multicast versions
   * @param model  - model of the game
   * @param reader - Multicast reader
   * @param gameToView - Game to view
   */
   public C_Player( C_PongModel model, NetMCReader reader, String gameToView  )
   {
	     
			 try {
				 	theSocket = new Socket(Global.TCP_SERVER_ADDR, Global.TCP_PORT);
				 	this.model = model;
					theOut = new NetTCPWriter(theSocket);
					theIn = reader;
					this.gameToView = Integer.parseInt(gameToView);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

     // ERASED code for constructor to save values for multicast read
   }

  public NetStringWriter getWriter()
  {
    return theOut;
  }

  public NetStringReader getReader()
  {
    return theIn;
  }


  /**
   * Get and update the model with the latest bat movement
   * sent by the server
   */
  public void run()                             // Execution
  {
    // Listen to network to get the latest state of the
    //  game from the server
    // Update model with this information, Redisplay model
    DEBUG.trace( "PlayerC.run" );
    try
    {
      while ( true )                           // Loop
      {
        String mes = theIn.get();              // From Server (State of the game)
        if ( mes == null ) break;              // No more data
        DEBUG.trace( "PlayerC.run Received [%s]", mes );
      
        GameObject bats[];
        GameObject ball;
        
        bats = model.getBats(); 
        ball = model.getBall();
        String gameState[]    = mes.split("\\|");          // deconstructing the state of the game
        
        String ballState   [] = gameState[0].split(" ");
        String player1State[] = gameState[1].split(" ");
        String player2State[] = gameState[2].split(" ");
        String       score [] = gameState[3].split(" ");
        //gameToView     = Integer.parseInt(gameState[4]);
        
        ball.setX(Double.parseDouble(ballState[0]));
        ball.setY(Double.parseDouble(ballState[1]));
        
        bats[0].setX(Double.parseDouble(player1State[0]));
        bats[0].setY(Double.parseDouble(player1State[1]));
        
        bats[1].setX(Double.parseDouble(player2State[0]));
        bats[1].setY(Double.parseDouble(player2State[1]));
        model.setScore(0, score[0]);
        model.setScore(1, score[1]);
        
        
        model.modelChanged();
      }

      theIn.close();                            // Close Read
      theOut.close();                           // Close Write

      theSocket.close();                        // Close Socket
    }
    catch ( Exception err )
    {
      err.printStackTrace();
      DEBUG.error( "PlayerC.run\n%s", err.getMessage() );
    }

  }
}
