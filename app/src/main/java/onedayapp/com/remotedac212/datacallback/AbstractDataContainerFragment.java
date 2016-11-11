package onedayapp.com.remotedac212.datacallback;

import onedayapp.com.remotedac212.fragments.*;

public abstract class AbstractDataContainerFragment implements DataCallback
{
  private String m_sendCmd;
  private String m_dataPrefix;
  private MainFragment m_Fragment;

  private int m_oneShot;

  public AbstractDataContainerFragment(MainFragment fragment, String sendCmd,
      String dataPrefix)
  {
    m_Fragment = fragment;
    m_sendCmd = sendCmd;
    m_dataPrefix = dataPrefix;

    m_oneShot = 0;
  }

  @Override
  public void setSendCommand(String cmd)
  {
    m_sendCmd = cmd;
  }

  protected MainFragment getMainFragment()
  {
    return m_Fragment;
  }

  protected void setOneShot(int oneShot)
  {
    m_oneShot = oneShot;
  }

  protected int getOneShot()
  {
    return m_oneShot;
  }

  @Override
  public String getSendCommand()
  {
    if (m_oneShot > 0)
    {
      return "";
    }
    return m_sendCmd;
  }

  @Override
  public void setDataReceivedPrefix(String prefix)
  {
    m_dataPrefix = prefix;
  }

  @Override
  public String getDataRecveivedPrefix()
  {
    return m_dataPrefix;
  }

  @Override
  public boolean terminating()
  {
    return false;
  }

}
