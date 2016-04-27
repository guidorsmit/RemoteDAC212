package onedayapp.com.remotedac212.settings;

public class VolumeHolder
{
  private static int maxMinVolume = 0;
  private static int maxMaxVolume = 0;

  public static void setMaxMinVolume(int volume)
  {
    if (maxMaxVolume < volume)
    {
      return;
    }

    if (0 <= volume)
    {
      return;
    }

    maxMinVolume = volume;
  }

  public static void setMaxMaxVolume(int volume)
  {
    if (maxMinVolume > volume)
    {
      return;
    }

    maxMaxVolume = volume;
  }

  public static int getMaxMinVolume()
  {
    return maxMinVolume;
  }

  public static int getMaxMaxVolume()
  {
    return maxMaxVolume;
  }
}
