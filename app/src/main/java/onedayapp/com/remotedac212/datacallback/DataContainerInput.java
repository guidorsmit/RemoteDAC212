package onedayapp.com.remotedac212.datacallback;

import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.states.*;

public class DataContainerInput extends AbstractDataContainerFragment
    implements DataCallback
{
  public DataContainerInput(MainFragment fragment, String sendCmd,
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

    for (InputControl ic : InputControl.getInputControlList())
    {
      if (ic.getState() == val)
      {
        setOneShot(1);
        fragment.updateInputControl(ic, false);
        return;
      }
    }
  }
}
