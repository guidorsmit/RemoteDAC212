package onedayapp.com.utils;

public class BooleanUtil
{
  public static boolean convert(int i)
  {
    if (i == 0)
    {
      return false;
    }
    return true;
  }

  public static int convert(boolean b)
  {
    if (!b)
    {
      return 0;
    }

    return 1;
  }
}
