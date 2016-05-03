
package onedayapp.com.remotedac212.datacallback;

import android.widget.*;

public class DataContainerChannelInputFrequency extends DataContainer
    implements DataCallback
{
  public DataContainerChannelInputFrequency(TextView tv, String sendCmd,
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
    int value;
    // value = Integer.valueOf(data);

    switch (data)
    {
    case "1":
      text = "PCM 22.05kHz";
      break;
    case "2":
      text = "PCM 24kHz";
      break;
    case "3":
      text = "PCM 32kHz";
      break;
    case "4":
      text = "PCM 44.1kHz";
      break;
    case "5":
      text = "PCM 48kHz";
      break;
    case "6":
      text = "PCM 88.2kHz";
      break;
    case "7":
      text = "PCM 96kHz";
      break;
    case "8":
      text = "PCM 176.4kHz";
      break;
    case "9":
      text = "PCM 192kHz";
      break;
    case "10":
      text = "PCM 352.8kHz";
      break;
    case "11":
      text = "PCM 384kHz";
      break;
    case "12":
      text = "DSD 64";
      break;
    case "13":
      text = "DSD 128";
      break;
    case "14":
      text = "DSD 256";
      break;
    case "15":
      text = "DSD 512";
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
