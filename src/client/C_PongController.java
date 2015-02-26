package client;

import common.*;

import java.awt.event.KeyEvent;

/**
 * Pong controller, handles user interactions
 */

public class C_PongController
{
  private C_PongModel model;
  private C_PongView  view;
  private NetStringWriter server = null;

  /**
   * Constructor
   * @param aPongModel Model of game on client
   * @param aPongView  View of game on client
   */
  public C_PongController( C_PongModel aPongModel, C_PongView aPongView)
  {
    model  = aPongModel;
    view   = aPongView;
    view.setPongController( this );  // View talks to controller
  }

  /**
   * Decide what to do for each key pressed
   * @param keyCode The keycode of the key pressed
   */
  public void userKeyInteraction(int keyCode )
  {
    // Key typed includes specials, -ve
    // Char is ASCII value
    switch ( keyCode )              // Character is
    {
      case -KeyEvent.VK_LEFT:        // Left Arrow
    	  server.put("LEFT");
        break;
      case -KeyEvent.VK_RIGHT:       // Right arrow
        break;
      case -KeyEvent.VK_UP:          // Up arrow
    	  server.put("UP");
        break;
      case -KeyEvent.VK_DOWN:        // Down arrow
    	  server.put("DOWN");
        break;
      case -KeyEvent.VK_SPACE:        // Down arrow
    	  server.put("SPACE");
        break;
    }
  }

  /*
   * Informs the controller the stream used to write to
   * the server, used for changes to bat position.
   * @param toServer Stream used to write to server
   */

  public void setWriter( NetStringWriter toServer )
  {
    server = toServer;
  }

}

