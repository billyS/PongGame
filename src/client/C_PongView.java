package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Observable;
import java.util.Observer;

import common.DEBUG;
import common.GameObject;
import common.Global;
import static common.Global.*;

/**
 * Displays a graphical view of the game of pong
 */
class C_PongView extends JFrame implements Observer
{
  private static final long	serialVersionUID	= 1L;
  private String name;
  private C_PongController pongController;
  private GameObject   ball;
  private GameObject[] bats;
  C_PongModel model = null;
  private Sound sound;

  public C_PongView( String name )
  {	  
      setSize( W, H );                        // Size of window
      addKeyListener( new Transaction() );    // Called when key press
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle( "Client " + name );
      sound = new Sound();
    
  }

  /**
   * Called from the model when its state is changed
   * @param aPongModel Model of the game
   * @param arg Argument passed not used
   */
  public void update( Observable aPongModel, Object arg )
  {
    model = (C_PongModel) aPongModel;
    ball  = model.getBall();
    bats  = model.getBats();
    name  = model.getGameName();
    DEBUG.trace( "serveroConsoleView.update" );
    repaint();                              // Re draw game
  }

  public void update( Graphics g )          // Called by repaint
  {
    drawPicture( (Graphics2D) g );          // Draw Picture
  }

  public void paint( Graphics g )           // When 'Window' is first
  {                                         //  shown or damaged
    drawPicture( (Graphics2D) g );          // Draw Picture
  }

  private Dimension     theAD;              // Alternate Dimension
  private BufferedImage theAI;              // Alternate Image
  private Graphics2D    theAG;              // Alternate Graphics

  /**
   * The code that actually displays the game graphically
   * @param g Graphics context to use
   */
  public void drawPicture( Graphics2D g )   // Double buffer
  {                                         //  allow re-size
    Dimension d    = getSize();             // Size of curr. image

    if (  ( theAG == null )           ||
        ( d.width  != theAD.width )   ||
        ( d.height != theAD.height ) )
    {                                       // New size
      theAD = d;
      theAI = (BufferedImage) createImage( d.width, d.height );
      theAG = theAI.createGraphics();
      AffineTransform at = new AffineTransform();
      at.setToIdentity();
      at.scale( ((double)d.width)/W, ((double)d.height)/H );
      theAG.transform(at);
    }

    drawActualPicture( theAG );             // Draw Actual Picture
    g.drawImage( theAI, 0, 0, this );       //  Display on screen
  }


  /**
   * Code called to draw the current state of the game
   *  Uses draw:       Draw a shape
   *       fill:       Fill the shape
   *       setPaint:   Colour used
   *       drawString: Write string on display
   * @param g Graphics context to use
   */
  public void drawActualPicture( Graphics2D g )
  {
    // White background
	  Rectangle2D.Double player1,player2,theBall,scoreline1,scoreline2;
	 
    g.setPaint( Color.white );
    g.fill( new Rectangle2D.Double( 0, 0, W, H) );
    Font font = new Font("Monospaced",Font.PLAIN,14);
    g.setFont( font );

    // Blue playing border

    g.setPaint( Color.blue );              // Paint Colour
    g.draw( new Rectangle2D.Double( B, M, W-B*2, H-M-B ) );
   
   
    // Display state of game
    if ( ball == null ) return;  // Race condition
    g.setPaint( Color.blue );
    
    FontMetrics fm = getFontMetrics( font );
    String fmt  = "Pong - Player 1 Score: [%s]" + " Player 2 Score: [%s]";
    String text = String.format( fmt, model.getScore(0), model.getScore(1));
    g.drawString( text, W/2-fm.stringWidth(text)/2, (int)M*2 );

    // The ball at the current x, y position (width, height)
    scoreline1 =  new Rectangle2D.Double( W-35, 0 + B, 10, H-B );
    scoreline2 =  new Rectangle2D.Double( 30, 0 + B, 10, H-B );
    player1 = new Rectangle2D.Double( bats[0].getX(), bats[0].getY(), BAT_WIDTH, BAT_HEIGHT );
	player2 = new Rectangle2D.Double( bats[1].getX(), bats[1].getY(), BAT_WIDTH, BAT_HEIGHT );
	theBall = new Rectangle2D.Double( ball.getX()   , ball.getY()   , BALL_SIZE, BALL_SIZE );

    g.setPaint( Color.red );
    //g.fill( new Rectangle2D.Double( ball.getX(), ball.getY(),
                                    //BALL_SIZE, BALL_SIZE ) );
    g.fill(theBall);
    g.setPaint( Color.blue );
    
    g.fill(player1);
    g.fill(player2);
    
    if(theBall.intersects(player1) || theBall.intersects(player2)){
    	sound.playSound("bounce.wav");
    }
    
    if( theBall.intersects(scoreline1) || theBall.intersects(scoreline2) ){
    	sound.playSound("cheering.wav");//TODO change to cheers
    }
  }

  /**
   * Need to be told where the controller is
   * @param aPongController The controller for the view
   */
  public void setPongController(C_PongController aPongController)
  {
    pongController = aPongController;
  }

  /**
   * Methods Called on a key press
   *  calls the controller to process key
   */
  class Transaction implements KeyListener  // When character typed
  {
    public void keyPressed(KeyEvent e)      // Obey this method
    {
      // Make -ve so not confused with normal characters
      pongController.userKeyInteraction( -e.getKeyCode() );
    }

    public void keyReleased(KeyEvent e)
    {
      // Called on key release including specials
    }

    public void keyTyped(KeyEvent e)
    {
      // Normal key typed
      pongController.userKeyInteraction( e.getKeyChar() );
    }
  }
}
