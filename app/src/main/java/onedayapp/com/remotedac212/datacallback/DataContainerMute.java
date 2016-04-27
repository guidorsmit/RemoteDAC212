package onedayapp.com.remotedac212.datacallback;

import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.states.*;

public class DataContainerMute extends AbstractDataContainerFragment
    implements DataCallback
{
  public DataContainerMute(MainFragment fragment, String sendCmd,
      String dataPrefix)
  {
    super(fragment, sendCmd, dataPrefix);
  }

  @Override
  public void onDataReceived(String data)
  {
    MainFragment fragment = getMainFragment();
    if (fragment == null)
    {
      return;
    }

    int val = Integer.valueOf(data);
    setOneShot(1);
    if (val == 0)
    {
      fragment.updateMuteState(MuteState.OFF);
      return;
    }

    fragment.updateMuteState(MuteState.ON);
  }
}
