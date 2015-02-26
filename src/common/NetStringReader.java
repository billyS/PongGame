package common;

/**
 * Implemented by classes that can write Strings
 *  across the network using a specific protocol
 *  protocols for read /and write must match
 */

public interface NetStringReader
{
  /**
    * Read a string from the network
    * @return string read or null if error 
    */
  public String get();

  /**
    * Close the stream
    */
  public void close();
}
