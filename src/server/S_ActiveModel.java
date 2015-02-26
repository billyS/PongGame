package server;

import static common.Global.*;
import common.*;

/**
 * A class used by the server model to give it an active part
 *  which continuously moves the ball and decides what to
 *  do on a collision
 */
class S_ActiveModel implements Runnable
{
  S_PongModel pongModel;
  double S = 0;

  public S_ActiveModel( S_PongModel aPongModel , double s)
  {
    DEBUG.assertTrue( aPongModel != null,
                      "ActiverModel constructor param null" );
    pongModel = aPongModel;
    S = s;
  }

  /**
   * Code to position the ball after time interval
   * runs as a separate thread
   */
  public void run()
  {
	 
    try
    {
      GameObject ball    = pongModel.getBall();
      GameObject bats[]  = pongModel.getBats();

      DEBUG.assertTrue( ball != null, "ActimeModel.run ball null" );
      DEBUG.assertTrue( bats != null, "ActimeModel.run bats array null" );
      DEBUG.assertTrue( bats[0] != null && bats[1] != null, "ActimeModel.run bats[0/1] null" );

      while (!Thread.currentThread().isInterrupted() )                                  //run until paused or connections lost
      {  
    		  double x = ball.getX(); double y = ball.getY();
    	        																		// Deal with possible edge of board hit
    		  
    	        if ( x >= W-B-BALL_SIZE) {												//Right
    	        	pongModel.setScore(0);												//Increment player 1 score 
    	        	ball.setX(bats[1].getX() - Global.BALL_SIZE);						//reset ball to player 2 bat
    	        	ball.setY(bats[1].getY() + bats[1].getHeight()/2);  
    	        	ball.changeDirectionX();											//change the direction of the ball
    	        	S = 0;																//stop the ball 
    	        }
    	        
    	        if ( x <= 0+B ) {														//Left
	        		pongModel.setScore(1);												//Increment player 2 score 
	        		ball.setX(bats[0].getX() + (Global.BALL_SIZE    -     Global.B ));  //reset ball to player 2 bat
    	        	ball.setY(bats[0].getY() + (bats[0].getHeight() / 2 - Global.B ));
    	        	ball.changeDirectionX();											//change the direction of the ball
	        		S = 0;																//stop the ball
    	        }
    	        
    	        if( S==0 && ball.getX() > 300 ){
    	        	
    	        	ball.setX(bats[1].getX() - Global.BALL_SIZE);						//ensure ball stays on player 2 bat until game starts
    	        	ball.setY(bats[1].getY() + bats[1].getHeight()/2);  	
    	        	
    	        }else if(S==0 && ball.getX() < 100 ){
    	        	
    	        	ball.setX(bats[0].getX() + (Global.BALL_SIZE    -     Global.B + 1)); //ensure ball stays on player 1 bat until game starts
    	        	ball.setY(bats[0].getY() + (bats[0].getHeight() / 2 - Global.B ));
    	        }
    	        
    	        if ( y >= H-B-BALL_SIZE ) ball.changeDirectionY(); 						// Bottom
    	        if ( y <= 0+M ) ball.changeDirectionY();								//Top
    	        
    	       
    	        ball.moveX( S );  ball.moveY( S );

    	        // As only a hit on the bat is detected it is assumed to be
    	        // on the front or back of the bat
    	        // A hit on the top or bottom has an interesting affect

    	        if ( bats[0].collision( ball ) == GameObject.Collision.HIT  ||
    	             bats[1].collision( ball ) == GameObject.Collision.HIT ) {
    	        	
    	        		ball.changeDirectionX();
    	        }

    	        pongModel.modelChanged();      // Model changed refresh screen

    	        Thread.sleep( 20 );            // About 50 Hz     
          }
    } catch ( Exception e ) {
      DEBUG.trace( "ActimeModel.run %s", e.getMessage() );
    }
  }
}

