package onedayapp.com.remotedac212.fragments;

import com.bluecreation.melody.*;

import android.bluetooth.*;
import android.content.*;

public class BluetoothBroadcastReceiver extends BroadcastReceiver
{

  @Override
  public void onReceive(Context context, Intent intent)
  {
    int newState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

    if (newState == BluetoothAdapter.STATE_OFF)
    {
      SppService.getInstance().stop();
    }
    else if (newState == BluetoothAdapter.STATE_ON)
    {
      SppService.getInstance().start();
    }
  }
}
