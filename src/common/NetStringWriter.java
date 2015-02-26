package common;
/**
 * Implemented by classes that can read Strings
 *  sent from across the network using  a specific protocol
 *  protocols for read /and write must match
 */

public interface NetStringWriter
{
  /**
   * Write a String to the network
   * @param message To be written
   * @return success/ failure
   */
  public boolean put( String message );

  /**
    * Close the stream
    */
  public void close();
}
