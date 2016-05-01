package onedayapp.com.remotedac212;

import java.text.*;
import java.util.*;

import com.bluecreation.melody.*;
import com.bluecreation.melody.SppService.*;
import onedayapp.com.remotedac212.datacallback.*;
import onedayapp.com.remotedac212.states.*;
import onedayapp.com.remotedac212.widgets.PagerAdapter;
import onedayapp.com.utils.*;

import android.bluetooth.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.util.Log;
import android.widget.*;

public class TabbedMainActivity extends FragmentActivity
{
  private boolean m_allowToSend = false;
  private static String m_delimiter = " ";
  private static String CR = "\r";

  // V2
  private PagerAdapter m_pagerAdapter;
  private ViewPager m_viewPager;

  private BluetoothAdapter m_bluetoothAdapter = null;
  // Member object for the melody services
  private SppService m_sppService = null;

  private Thread m_updateThread;
  private boolean m_stopUpdateThread = false;;

  private String m_receiveData;
  private long m_blockedTime;
  private static String s_volumePrefix;

  private List<DataCallback> m_callbackList;

  // Update speed of the thread. time in MS
  private static int UPDATE_SPEED = 500;
  // Time blocked not receiving volume packets. time in MS
  private static int BLOCKED_TIME = 2000;
  // Nr of pages in the app, Keep in mind that the pageAdapter needs to be
  // updated aswel
  private static int NR_OF_PAGES = 2;

  // Message represents the state of the connection
  private String m_connectMsg;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_tabbed);

    m_callbackList = new ArrayList<DataCallback>();

    m_allowToSend = false;
    m_blockedTime = 0;
    // Get the volume prefix -- for the hack
    s_volumePrefix = getResources().getString(R.string.volume_prefix);

    m_connectMsg = getResources().getString(R.string.state_not_connected);

    // ViewPager and its adapters use support library
    // fragments, so use getSupportFragmentManager.
    m_pagerAdapter = new PagerAdapter(getSupportFragmentManager(), NR_OF_PAGES);
    m_viewPager = (ViewPager) findViewById(R.id.pager);
    m_viewPager.setAdapter(m_pagerAdapter);

    m_receiveData = "";
    m_updateThread = new Thread(new Runnable()
    {

      @Override
      public void run()
      {
        List<DataCallback> clonedList;
        while (!m_stopUpdateThread)
        {
          clonedList = new ArrayList<DataCallback>(m_callbackList);

          for (DataCallback dc : clonedList)
          {
            if (dc.terminating())
            {
              m_callbackList.remove(dc);
              continue;
            }

            send(dc.getSendCommand());
            try
            {
              // Sleep for 100 ms
              Thread.sleep(UPDATE_SPEED);
            }
            catch (InterruptedException ie)
            {
              ie.printStackTrace();
            }
          }

        }
      }
    });

    m_updateThread.start();
  }

  @Override
  public void onStart()
  {
    if (m_sppService == null)
    {
      m_sppService = SppService.getInstance();
      m_sppService.registerListener(m_sppListener);
    }
    super.onStart();
  }

  public void gotoNextPage()
  {
    m_viewPager.setCurrentItem(m_viewPager.getCurrentItem() + 1);
  }

  public void gotoScanPage()
  {
    m_allowToSend = false;
    // Stop bluetooth Serial port profile
    if (m_sppService != null)
    {
      m_sppService.unregisterListener(m_sppListener);
      if (m_allowToSend)
      {
        m_sppService.disconnect();
      }
      m_sppService.stop();
      m_sppService = null;
    }

    m_viewPager.setCurrentItem(0);
  }

  public String getConnectMsg()
  {
    return m_connectMsg;
  }

  private void setStateNotConnected()
  {
    m_connectMsg = getResources().getString(R.string.state_not_connected);
    m_pagerAdapter.getConnectFragment().updateConnectState();
  }

  private void setStateConnecting(String deviceName)
  {
    m_connectMsg = getResources().getString(R.string.state_connecting) + deviceName;
    m_pagerAdapter.getConnectFragment().updateConnectState();
  }

  private void setStateConnected(String deviceName)
  {
    m_connectMsg = getResources().getString(R.string.state_connected) + deviceName;
    m_pagerAdapter.getConnectFragment().updateConnectState();
  }



  public void connect(String deviceName, String deviceAddress)
  {
    // Get the data from the ScanActivity
    //
    if (StringUtil.isEmpty(deviceAddress))
    {
      return;
    }
    // V2
    // Initialize the MelodySppService to perform bluetooth connections
    connectDevice(deviceAddress);

    // Connect to the bluetooth device
    setStateConnecting(deviceName);
    System.out.println("Connection to: " + deviceName);
  }

  // V2
  private void connectDevice(String address)
  {
    if (m_sppService == null)
    {
      m_sppService = SppService.getInstance();
      // m_sppService.disconnect();
      m_sppService.registerListener(m_sppListener);
      m_sppService.start();
    }

    // Get the device MAC address
    if (BluetoothAdapter.checkBluetoothAddress(address))
    {
      // Get local Bluetooth adapter
      m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

      // Get the BluetoothDevice object
      BluetoothDevice device = m_bluetoothAdapter.getRemoteDevice(address);
      // Attempt to connect to the device
      m_sppService.connect(device);
    }
  }

  @Override
  public void onDestroy()
  {
    try
    {
      // Stop bluetooth Serial port profile
      if (m_sppService != null)
      {
        m_sppService.unregisterListener(m_sppListener);
        if (m_allowToSend)
        {
          m_sppService.disconnect();
        }
        m_sppService.stop();
        m_sppService = null;
      }

      if (m_updateThread != null)
      {
        m_stopUpdateThread = true;
      }
      m_allowToSend = false;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onDestroy();
  }

  public boolean send(String command)
  {
    if (!m_allowToSend)
    {
      // System.out.println("Not allowed to send... sorry not connected yet");
      return false;
    }

    if (StringUtil.isEmpty(command))
    {
      return false;
    }

    if (!command.endsWith(CR))
    {
      command += CR;
    }

    // V2
    if (m_sppService == null)
    {
      return false;
    }
    System.out.println("Command to send: " + command);
    m_sppService.send(command.getBytes());
    return true;
  }

  public boolean sendVolume(int volume)
  {
    String cmd;
    if (volume > 0)
    {
      cmd = getResources().getString(R.string.volume_up_prefix);
    }
    else if (volume < 0)
    {
      cmd = getResources().getString(R.string.volume_down_prefix);
    }
    else
    {
      cmd = "";
    }

    setBlocked();
    return send(cmd);
  }

  public boolean sendAbsoluteVolume(int volume)
  {
    String cmd;
    cmd = getResources().getString(R.string.set_volume_prefix);
    cmd += volume;

    setBlocked();
    return send(cmd);
  }

  private void setBlocked()
  {
    // HACK do not listen to updates for volume
    // Set blocked
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    m_blockedTime = calendar.getTimeInMillis() + BLOCKED_TIME;
  }

  private boolean isBlocked()
  {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return calendar.getTimeInMillis() <= (m_blockedTime + BLOCKED_TIME);
  }

  public boolean sendMute(MuteState mute)
  {
    String prefix = getResources().getString(R.string.set_mute_prefix);
    if (mute == MuteState.ON)
    {
      return send(prefix + "1");
    }
    return send(prefix + "0");
  }

  public void addDataCallback(DataCallback callback)
  {
    if (callback == null)
    {
      return;
    }

    m_callbackList.add(callback);
  }

  /**
   * Set power mode of the device: 0=off 1=on
   * 
   * @param power
   * @return {@link Boolean} true if the command was succesful
   */
  public boolean sendPower(PowerState power)
  {
    String prefix = getResources().getString(R.string.set_power_prefix);

    String cmd = prefix + "1";
    if (power == PowerState.OFF)
    {
      cmd = prefix + "0";
    }

    return send(cmd);
  }

  public boolean sendInputControl(InputControl inputControl)
  {
    // ￼AUDIO=(value)

    int channel;
    String prefix = getResources().getString(R.string.set_input_prefix);
    channel = inputControl.getState();
    // channel = 5;
    // updateInputControl(inputControl);
    return send(prefix + channel);
  }

  /**
   * SPP listener (Serial Port Profile) <br>
   * Receives all bluetooth commands and also sends them
   */
  private SppService.Listener m_sppListener = new SppService.Listener()
  {
    @Override
    public void onStateChanged(final ConnectionState state)
    {
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {

          if (state == ConnectionState.STATE_CONNECTED)
          {
            m_allowToSend = true;
            // mInEditText.setText("", TextView.BufferType.EDITABLE);
            // in debug mode
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                .format(new Date());
            // send(s);
          }
          else
          {
            // gotoScanPage();
            setStateNotConnected();
            m_allowToSend = false;
          }
        }
      });
    }

    @Override
    public void onRemoteDeviceConnected(String deviceName)
    {
      setStateConnected(deviceName);

      final String deviceNam = deviceName;
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          Toast.makeText(getApplicationContext(),
              "Connection to " + deviceNam + " Successful", Toast.LENGTH_SHORT)
              .show();
        }
      });
    }

    @Override
    public void onDataReceived(final byte[] data, final int length)
    {
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          List<String> split;
          String readData = new String(data, 0, length);

          // System.out.print("Incomming:" + readData);
          // System.out.print("Length:" + length);

          if (StringUtil.contains(readData, CR))
          {
            String received = "";

            split = StringUtil.split(readData, CR);
            received = m_receiveData;
            received += split.get(0);

            if (split.size() > 1)
            {
              m_receiveData = split.get(1);
            }
            else
            {
              m_receiveData = "";
            }

            // String completed parse data
            // Update buttons
            split = StringUtil.split(received, m_delimiter);
            if ((split.size() <= 1))
            {
              System.out.println("Incomplete: " + received);
              return;
            }
            String result = split.get(1);
            if (StringUtil.isEmpty(result))
            {
              return;
            }

            // make sure there is no CR included in the result
            result = StringUtil.split(result, CR).get(0);
            if (StringUtil.isEmpty(result))
            {
              return;
            }

            System.out.println("Received:" + received);
            // Apply HACK check if we are blocked and received volume packed
            if ((StringUtil.startsWith(received, s_volumePrefix)
                || StringUtil.startsWith(received, "%" + s_volumePrefix))
                && isBlocked())
            {
              // We have send a volume command within the
              return;
            }

            for (DataCallback dc : m_callbackList)
            {
              if (StringUtil.startsWith(received, dc.getDataRecveivedPrefix())
                  || StringUtil.startsWith(received,
                      "%" + dc.getDataRecveivedPrefix()))
              {
                dc.onDataReceived(result);
              }
            }
            return;
          }
          m_receiveData += readData;
          // System.out.println("Incomplete red: " + m_receiveData);

          // Toast.makeText(getApplicationContext(), readMessage,
          // Toast.LENGTH_SHORT).show();
        }
      });
    }

    @Override
    public void onConnectionLost()
    {
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          Toast.makeText(getApplicationContext(), "Connection Lost",
              Toast.LENGTH_SHORT).show();
          setStateNotConnected();

          gotoScanPage();
        }
      });
    }

    @Override
    public void onConnectionFailed()
    {
      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          Toast.makeText(getApplicationContext(), "Connection Failed",
              Toast.LENGTH_SHORT).show();
          setStateNotConnected();

          gotoScanPage();
        }

      });
    }
  };

}