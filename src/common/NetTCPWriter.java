package common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Wrapper for writing Strings to a socket
 */

public class NetTCPWriter extends ObjectOutputStream implements NetStringWriter
{
  /**
   * @param s Socket to write to.
   * @throws IOException when can not create the connection
   */
  public NetTCPWriter( Socket s ) throws IOException
  {
    super( s.getOutputStream() );
    DEBUG.traceA( "new NetTCPStringWriter()" );
    s.setTcpNoDelay(true);              // Send data immediately
  }

  // write object to socket returning false on error
  public synchronized boolean put( String data )
  {
    try
    {
      writeObject( data );                    // Write object
      flush();                                // Flush
      return true;                            // Ok
    }
    catch ( IOException e )
    {
      DEBUG.trace("NetObjectWriter.get %s", e.getMessage());
      return false;                           // Failed write
    }
  }

  public void close()
  {
    try
    {
      super.close();
    } catch ( Exception e )
    {
      DEBUG.error("NetTCPWriter.close %s", e.getMessage() );
    }
  }
}

