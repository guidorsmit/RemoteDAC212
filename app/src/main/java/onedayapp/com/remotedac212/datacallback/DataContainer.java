
package onedayapp.com.remotedac212.datacallback;

import android.widget.*;

public class DataContainer implements DataCallback
{
  private String m_sendCmd;
  private String m_dataPrefix;
  private TextView m_tv;

  public DataContainer(TextView tv, String sendCmd, String dataPrefix)
  {
    m_tv = tv;
    m_sendCmd = sendCmd;
    m_dataPrefix = dataPrefix;
  }

  public TextView getTextView()
  {
    return m_tv;
  }

  @Override
  public void setSendCommand(String cmd)
  {
    m_sendCmd = cmd;
  }

  @Override
  public String getSendCommand()
  {
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
  public void onDataReceived(String data)
  {
    if (m_tv == null)
    {
      return;
    }

    m_tv.setText(data);
  }

  @Override
  public boolean terminating()
  {
    return false;
  }

}
