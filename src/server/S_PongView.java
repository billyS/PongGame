package server;

import common.*;

import java.util.Observable;
import java.util.Observer;

/**
 * Displays a graphical view of the game of pong
 */
class S_PongView implements Observer
{
  private S_PongController pongController;
  private GameObject   ball;
  private GameObject[] bats;
  private NetStringWriter left  = null;
  private NetStringWriter right = null;
  private NetStringWriter mc = null;
  private int gameNumber = 0;
  private double delay = 0;
  private String role="";

  /**
   * Constructor
   */
  public S_PongView(String role)
  {
    Counter.inc();              // next Game identifier
    gameNumber = Counter.get(); // Remember
    this.role = role;
  }

  public void setStreams( NetStringWriter c1, NetStringWriter c2, NetStringWriter mc )
  {
    DEBUG.assertTrue( c1 != null && c2!= null,// && mc!=null,
                      "S_PongView.setStreams - c1,c2  or c3 null" );
    left = c1; right = c2; this.mc = mc;
  }

  public void setStreams( NetStringWriter mc )
  {
    DEBUG.assertTrue( mc != null,
                      "S_PongView.setStreams - mc null" );
    this.mc = mc;
  }

  /**
   * Called from the model when its state is changed
   * @param aPongModel Model of game
   * @param arg Arguments - not used
   */
  public void update( Observable aPongModel, Object arg)
  {
    S_PongModel model = (S_PongModel) aPongModel;
   
    ball  = model.getBall();
	bats  = model.getBats();
	
	DEBUG.assertTrue( ball != null, "S_PongView.update ball null" );
	DEBUG.assertTrue( bats != null, "S_PongView.update bats array null" );
	DEBUG.assertTrue( bats[0] != null && bats[1] != null, "S_PongView.update bats[0/1] null" );
	
	String state = ball.getX()    		+ " " + ball.getY()       + "|" + 
			       bats[0].getX() 		+ " " + bats[0].getY()    + "|" + 
			       bats[1].getX() 		+ " " + bats[1].getY()    + "|" +
			       model.getScore(0) 	+ " " + model.getScore(1) + "|" +
			       model.getGameId(); 
			

	//String player1State =  state + model.getClientTime(0) + "|" + model.getGameId() + "|" + model.getScore(0); 
	
	//String player2State =  state + model.getClientTime(1) + " " + model.getGameId() + "|" + model.getScore(1);
	
	//String player1State =  state + "|" + model.getScore(0); 
	
	//String player2State =  state + "|" + model.getScore(1);
	
	
	double pingP1 =0;// model.getPing(0);						// ping
	double pingP2 = 0;//model.getPing(1);						// ping
	DEBUG.trace("Ping from clients", String.valueOf(pingP1),String.valueOf(pingP2));
	int maxNrOfGames = S_PongModel.nextId.get()-1; 		//reduce the amount of games 
	if(Integer.parseInt(model.getScore(0)) == 10 || Integer.parseInt(model.getScore(1)) == 10){
		//TODO send game over to clients
	}
	
	switch (role) {
	case "MC":
		mc.put(state + " " + model.getGameId() +" "+ maxNrOfGames);
		break;

	default:
		try {
			
			if (pingP1 == pingP2)  {
				
				left.put(  state  );
				right.put( state );
			} 
			else if ( pingP1 > pingP2 ) {// Delay  p 1 
			
				delay = pingP1 - pingP2;
				left.put(  state  );
				Thread.sleep((long) delay);
				right.put( state  );
			}else  {
				delay = pingP2 - pingP1; // Delay  p 2
				right.put( state );
				Thread.sleep((long) delay);
				left.put( state  );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		break;
	}

  }

}
