package onedayapp.com.remotedac212.settings;

/**
 * Store all the settings toward the DAC212
 * 
 * @author GRS
 * 
 */
public class Settings
{
  private int m_volume;
  private int m_input;
  private boolean m_mute;
  private int m_baudrate;

  // Use singleton construction
  private static Settings s_settings;

  public static Settings getSettings()
  {
    if (s_settings == null)
    {
      s_settings = new Settings();
    }
    return s_settings;
  }

  private Settings()
  {
    m_volume = 0;
    m_input = 0;
    m_mute = false;
    m_baudrate = 9600;
  }

  public int getVolume()
  {
    return m_volume;
  }

  public void setVolume(int volume)
  {
    m_volume = volume;
  }

  public int getInput()
  {
    return m_input;
  }

  public void setInput(int input)
  {
    m_input = input;
  }

  public boolean isMuted()
  {
    return m_mute;
  }

  public void setMuted(boolean mute)
  {
    m_mute = mute;
  }

  public int getBaudrate()
  {
    return m_baudrate;
  }

  public void setBaudrate(int baudrate)
  {
    m_baudrate = baudrate;
  }
}
