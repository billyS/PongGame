package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
class StartView extends JFrame 
{  
  private static final long serialVersionUID = 1L;
  private JButton tcpButton;
  private JButton mcButton;
  
  public static void main( String args[] )
  {
	 new StartView() ;
  }
  
  public StartView() {
	  
	  JPanel panel = new JPanel();
	  
	  tcpButton = new JButton("Connect Via TCP");
	  tcpButton.setBounds(20, 20, 60, 20);
	  
	  mcButton = new JButton("Connect Via MC");
	  mcButton.setBounds(60, 80, 60, 20);
	  
	  tcpButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			(new Client("TCP")).start();
			
		}
	  });

	  mcButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Client mcClient = new Client("MC");
		}
	});
	
	
	  panel.add(tcpButton);
	  panel.add(mcButton);
	  
	  
	  add(panel);
      setSize( W, H );                        // Size of window
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
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
    g.setPaint( Color.white );
    g.fill( new Rectangle2D.Double( 0, 0, W, H) );
     
  }
}
