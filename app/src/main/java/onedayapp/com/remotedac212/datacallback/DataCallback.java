package onedayapp.com.remotedac212.datacallback;

public interface DataCallback
{
  public void setSendCommand(String cmd);

  public String getSendCommand();

  public void setDataReceivedPrefix(String prefix);

  public String getDataRecveivedPrefix();

  public void onDataReceived(String data);

  public boolean terminating();
}
