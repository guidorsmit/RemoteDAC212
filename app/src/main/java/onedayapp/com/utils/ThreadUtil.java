package onedayapp.com.utils;

public class ThreadUtil
{
  /**
   * Add a runnable to the workerthread
   * 
   * @param runnable
   */
  public static void runRunnable(Runnable runnable)
  {
    Thread t = new Thread(runnable, "WorkerThread-1");
    try
    {
      t.start();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
