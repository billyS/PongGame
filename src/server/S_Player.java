package server;

import java.io.IOException;
import java.net.*;

import static common.Global.*;
import common.*;

/**
 * Individual player run as a separate thread to allow
 * updates to the model when a player moves there bat
 */
class S_Player extends Thread
{
  private Socket          theSocket;         // Socket used
  private NetStringReader theIn;             // Input object stream
  private NetStringWriter theOut;            // Output object stream
  private int             playerNum;
  private S_PongModel     model;
  private int counter;

  /**
   * Constructor
   * @param player PlayerS 0 or 1
   * @param model Model of the game
   * @param in Reader
   */
  public S_Player( int player, S_PongModel model, NetStringReader reader)
  {
    this.model     = model;
	playerNum      = player;
	theIn 	  	   = reader;
    DEBUG.trace( "PlayerS.constuctor %d", playerNum );
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
   */
  public void run()                            // Execution
  {
	
    try
    {
      while ( true )                           // Loop
      {  
    	  String move = theIn.get();             // From Client
    	  if ( move == null ) break;             // No more data
    	  DEBUG.trace( "S Move %s", move );
 
		switch (move) {
		case "UP":
			model.moveBatUp(playerNum);
			break;	
		case "DOWN":
			model.moveBatDown(playerNum);
			break;
		case "SPACE":
			if(counter == 0) {
				model.pauseGame();
				counter++;
			}else {
				counter = 0;
				model.resumeGame();
			}
			break;
		case "LEFT":
			model.setSpeed(4);
			break;
		}
		
		 model.modelChanged();
      }

      theIn.close();                            // Close Read
      theOut.close();                           // Close Write

      theSocket.close();                        // Close Socket
    }
    catch ( Exception err )
    {
      DEBUG.error( "PlayerS.run [%d]\n%s",
                   playerNum, err.getMessage() );
    }
  }
}
