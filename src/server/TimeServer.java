package server;

import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import common.Counter;
import common.Global;
import common.NetStringReader;
import common.NetStringWriter;
import common.NetTCPReader;
import common.NetTCPWriter;

public class TimeServer
{
  private DeliverContent dc;
  private S_PongModel model;
  private int counter=0;
  private ServerSocket ss;
  private int bat = 0;
  
  public TimeServer( S_PongModel model, int bat, ServerSocket ss )
  {
	  this.model = model; this.ss = ss; this.bat = bat;
  }

  public void process() 
  {
    try
    {
      //ServerSocket ss = new ServerSocket(50003);   						// Server Socket
      //this.model = model;
      while( true )                               						// Loop
      {
    	//System.out.println("Server started listening on port 50003");
        Socket socket  = ss.accept();  
        
        dc = new DeliverContent(socket, model, counter);     							//
        																// Run in parallel (thread) or sequentially
        dc.start();														//  Start thread
        
        if(counter < 1) {
        	counter++;
        }else {
        	counter = 0;
        }
      }
    }
    catch ( Exception err )
    {
    	err.printStackTrace();
    }
  }
}


class DeliverContent extends Thread
{
  private Socket theSocket;                      					// Socket used
  private NetStringReader  theIn;                 				// Input stream
  private NetStringWriter theOut;
  private S_PongModel model;
  private int playerNum;
  
  public DeliverContent( Socket s, S_PongModel model, int counter)
  {
    theSocket = s;                               					// Socket used   
    this.model = model;
    playerNum = counter;
  }

  public void run()                              					// Execution
  {
    try
    {
      theOut = new NetTCPWriter(theSocket); 	// Output
      theIn  = new NetTCPReader(theSocket);  	// Input
     
      while ( true )
      {
    	 requestTime();                      	 			// request Client Time
    	 long start = System.nanoTime();
    	 
         String message = theIn.get();       				// From Client
         
         if ( message == null ) break;           			// No more data
         
         long stop = System.nanoTime();
		 double tt = (double) (stop-start)/1_000_000_000; 	//calculate ping as seconds
		 String key = String.valueOf(model.hashCode())+String.valueOf(playerNum);
		 model.setPing(key,String.valueOf(tt));
   	  	 System.out.printf("It took %12.9f seconds\n", tt );
		 System.out.println("Client Time: " + message );
		 System.out.println("model Hash code: " + key);
		 //System.out.println("Model  Ping: " + String.valueOf(model.getPing(1)) );
   	  	 Thread.sleep(2000);
      }
      
      theIn.close();                             			// Close Read
      theOut.close();                            			// Close Write
      theSocket.close();                         			// Close Socket
    }
    catch( Exception err ) {}
    //DEBUG.trace( "Connect", "Closed the connection" );

  }

  
  public void requestTime() throws IOException
  {
      theOut.put("T");
  }
}

