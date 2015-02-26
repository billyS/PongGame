package common;

import java.net.SocketImpl;

/**
 * Major constants used in the game
 */
public class Global
{
  public static final int H = 450;          // Height of window
  public static final int W = 600;          // Width  of window

  public static final double B = 6;            // Border offset
  public static final double M = 26;           // Menu offset
  public static final double BALL_SIZE  = 15;  // Ball side
  public static final double BAT_WIDTH  = 10;  // Bat width
  public static final double BAT_HEIGHT = 100; // Bat Height

  public static final double BAT_MOVE=7;       // Each move is

  // Of course the following should not be a constant
  //  but should be user changeable

  // TCP/IP addresses
  public static final int    TCP_PORT        = 50001;
  public static final String TCP_SERVER_ADDR = "localhost";

  // MultiCast addresses
  public static final String MC_ADDR = "224.0.0.7";
  public static final int    MC_PORT = 50002;

  public static final boolean DEBUG_STATE_SERVER = true;
  public static final boolean DEBUG_STATE_CLIENT = true;
  public static final int THREAD_POOL = 6;


}
