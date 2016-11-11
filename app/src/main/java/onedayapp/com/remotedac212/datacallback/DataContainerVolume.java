package onedayapp.com.remotedac212.datacallback;

import java.util.*;

import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.settings.*;

/**
 * Special data container for volume contains min max current
 * 
 * @author GRS
 *
 */
public class DataContainerVolume extends AbstractDataContainerFragment
    implements DataCallback
{
  private int m_count = 0;
  private List<String> m_cmdList;

  private int m_currentVolume;

  public DataContainerVolume(MainFragment fragment, String sendCmd,
      String dataPrefix)
  {
    super(fragment, sendCmd, dataPrefix);
    m_cmdList = new ArrayList<String>();
    m_cmdList.add(":VOLU?N");
    m_cmdList.add(":VOLU?M");
    m_cmdList.add(":VOLU?");

    m_currentVolume = 0;
  }

  @Override
  public void onDataReceived(String data)
  {
    boolean skip = true;
    MainFragment fragment = getMainFragment();
    if (fragment == null)
    {
      return;
    }

    setOneShot(1);
    m_currentVolume = Integer.valueOf(data);

    if (skip)
    {
      try
      {
        switch (m_count)
        {
        case 0:
          VolumeHolder.setMaxMinVolume(Integer.valueOf(data));
          break;
        case 1:
          VolumeHolder.setMaxMaxVolume(Integer.valueOf(data));
          break;
        case 2:
        default:
          m_currentVolume = Integer.valueOf(data);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    fragment.setCurrentVolume(m_currentVolume);
    fragment.updateVolume(null);
  }
}
