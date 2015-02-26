package common;

import java.net.*;
import java.io.*;

public class NetMCReader implements NetStringReader
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  /**
   * @param aPort Port to read from
   * @param mca   Multicast address
   * @throws IOException when can not create the connection
   */
  public NetMCReader(int aPort, String mca ) throws IOException
  {
    port   = aPort;
    DEBUG.traceA("MCRead: C port [%s] MCA [%s]", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.joinGroup(group);
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

  public synchronized String get()
  {
    try
    {
      DEBUG.trace("MCRead: on port [%d]", port );
      byte[] buf = new byte[512];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

      String m = new String( packet.getData(), 0, packet.getLength() );
      DEBUG.trace("MCRead: Read <%s>", m );
      return m;
     } catch ( Exception e )
     {
       DEBUG.trace("MCRead: Read Failure %s", e.getMessage() );
       return null;
     }
  }
}
