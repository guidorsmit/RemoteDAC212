package onedayapp.com.remotedac212.datacallback;

import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.states.*;

public class DataContainerPower extends AbstractDataContainerFragment
    implements DataCallback
{

  public DataContainerPower(MainFragment fragment, String sendCmd,
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
      fragment.updatePowerState(PowerState.OFF);
      return;
    }

    fragment.updatePowerState(PowerState.ON);
  }

}
