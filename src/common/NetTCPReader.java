package common;

import java.io.*;
import java.net.Socket;

/**
 * Wrapper to allow reading of Strings from a socket
 */

public class NetTCPReader extends ObjectInputStream implements NetStringReader
{
  /**
   * @param s Socket to read from
   * @throws IOException when can not create the connection
   */
  public NetTCPReader( Socket s ) throws IOException
  {
    super( s.getInputStream() );
    DEBUG.traceA( "new NetTCPStringReader()" );
  }

  // Get object return null on  'error'
  public synchronized String get()           // Get object from stream
  {
    try                                      //
    {
      String s = (String) readObject();          // Return read object
      DEBUG.trace("NetTCPReader.got [%s]", s);
      return s;
    }
    catch ( Exception e )                    // Reading error
    {
      e.printStackTrace();
      DEBUG.error("NetTCPReader.get %s", e.getMessage() );
      return null;                           //  On error return null
    }
  }

  public void close()
  {
    try
    {
      super.close();
    } catch ( Exception e )
    {
      DEBUG.error("NetTCPReader.close %s", e.getMessage() );
    }
  }
}


