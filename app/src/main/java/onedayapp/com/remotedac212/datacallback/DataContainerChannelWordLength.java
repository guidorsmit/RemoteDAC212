
package onedayapp.com.remotedac212.datacallback;

import android.widget.*;

public class DataContainerChannelWordLength extends DataContainer
    implements DataCallback
{
  public DataContainerChannelWordLength(TextView tv, String sendCmd,
      String dataPrefix)
  {
    super(tv, sendCmd, dataPrefix);
  }

  @Override
  public void onDataReceived(String data)
  {
    if (getTextView() == null)
    {
      return;
    }

    String text;
    // value = Integer.valueOf(data);

    switch (data)
    {
    case "1":
      text = "16bits";
      break;
    case "2":
      text = "17bits";
      break;
    case "3":
      text = "18bits";
      break;
    case "4":
      text = "19bits";
      break;
    case "5":
      text = "20bits";
      break;
    case "6":
      text = "21bits";
      break;
    case "7":
      text = "22bits";
      break;
    case "8":
      text = "23bits";
      break;
    case "9":
      text = "24bits";
      break;
    case "10":
      text = "32bits";
      break;
    default:
      text = "";

    }
    getTextView().setText(text);
  }

  @Override
  public boolean terminating()
  {
    return false;
  }

}
