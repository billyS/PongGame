package common;

import java.net.*;
import java.io.*;

/**
 * Datagram writer
 */
public class NetMCWriter implements NetStringWriter
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  /**
   * @param aPort Port to write to
   * @param mca   Multicast address
   * @throws IOException when can not create the connection
   */
  public NetMCWriter(int aPort, String mca) throws IOException
  {
    port = aPort;
    DEBUG.traceA( "NetMCWrite: port [%5d] MCA [%s]", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.setTimeToLive(40); 
  }

  public void close()
  {
    try
    {
      socket.leaveGroup(group);
      socket.close();
    } catch ( Exception e )
    {
	  DEBUG.error("NetTCPReader.close %s", e.getMessage() );
	}
  }

  public synchronized boolean put( String message )
  {
    try
    {
      DEBUG.trace("MCWrite: port [%5d] <%s>", port, message );

      byte[] buf = message.getBytes();
      DatagramPacket packet =
        new DatagramPacket(buf, buf.length, group, port);
      socket.send(packet);
      return true;
    } catch ( Exception e )
    {
      DEBUG.error( "NetMCWriter.put() " + e.getMessage() );
      return false;
    }
    
  }
}
