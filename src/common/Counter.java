package common;

/**
 * A global counter
 */

public class Counter
{
  static int aCounter = 0;

  public static void inc()
  {
    aCounter++;
  }
  
  public static int get()
  {
    return aCounter;
  }
}
