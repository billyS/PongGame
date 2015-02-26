package server;

import common.*;
import static common.Global.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Model of the game of pong
 *  The active object ActiveModel does the work of moving the ball
 */
public class S_PongModel extends Observable
{
  private GameObject ball   = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
  private GameObject bats[] = new GameObject[2];
  private Map<String,String> ping; 
  static AtomicInteger nextId = new AtomicInteger(); 

  private Thread activeModel; 
  private int id;
  private Future<?> future;
  private String score[] ={"0","0"};
  private ExecutorService es;
  private double s;
 

public S_PongModel()
  {
    bats[0] = new GameObject(  60, H/2, BAT_WIDTH, BAT_HEIGHT);
    bats[1] = new GameObject(W-60, H/2, BAT_WIDTH, BAT_HEIGHT);
    ping    = new HashMap<String, String>();
	id      = nextId.incrementAndGet(); // every new model = new 2player game created so users can observe a specific game
	s       = 4;
    activeModel = new Thread( new S_ActiveModel( this , s) );
  }

  /**
   * Start the thread that moves the ball and detects collisions
   */
  public void makeActiveObject(ExecutorService es)
  {
	  this.es = es;
	  future  = es.submit(activeModel);
  }

  /**
   * Return the Game object representing the ball
   * @return the ball
   */
  public GameObject getBall()
  {
    return ball;
  }

  /**
   * Set a new Ball object
   * @param aBall - Ball to be set
   */
  public void setBall( GameObject aBall ) {
    ball = aBall;
  }

  /**
   * Return the Game object representing the Bat for player
   * @param player 0 or 1
   * @return a gane object
   */
  public GameObject getBat(int player )  {
    return bats[player];
  }

  /**
   * Return the Game object representing the Bats
   * @return Array of two bats
   */
  public GameObject[] getBats() {
    return bats;
  }

  /**
   * Set the Bat for a player
   * @param player  1 or 2
   * @param theBat  Players Bat
   */
  public void setBat( int player, GameObject theBat ) {
    bats[player] = theBat;
  }

  /**
   * Cause update of view of game
   */
  public void modelChanged()  {
    DEBUG.trace( "S_PongModel.modelChanged");
    setChanged(); notifyObservers();
  }
  
  public void moveBatDown( int player ) {
		if(!(bats[player].getY()>(H - BAT_HEIGHT - B)))
			bats[player].setY( bats[player].getY() + BAT_MOVE );
  }


   public void moveBatUp( int player ) {
		if(!(bats[player].getY()<(B+M)))
			bats[player].setY( bats[player].getY() - BAT_MOVE );
   }


   public void setPing(String player, String thePing) {
	  ping.put(player, thePing);
		
   }


  public String getPing(int player) { 
		return ping.get(String.valueOf(player));
   }


   public int getGameId() {
		return id;
   }
	
   public void pauseGame() {
		future.cancel(true);
   }
	
   public void resumeGame() {
	   future = es.submit(activeModel);
   }

   public String getScore(int playerNum) {
	
	return score[playerNum];
   }
   
   public void setScore(int playerNum) {
	   int temp = Integer.parseInt(score[playerNum]);
	   temp++;
	   score[playerNum] = String.valueOf(temp); 
   }
   
   public void setSpeed(double speed) {
	   future.cancel(true);
	   activeModel = new Thread( new S_ActiveModel( this , speed) );
	   future = es.submit(activeModel);
   }
   
}
