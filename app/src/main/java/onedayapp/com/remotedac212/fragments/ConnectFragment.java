package onedayapp.com.remotedac212.fragments;

import java.lang.reflect.*;
import java.util.*;

import onedayapp.com.remotedac212.*;
import onedayapp.com.remotedac212.settings.*;
import onedayapp.com.utils.*;

import android.bluetooth.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class ConnectFragment extends Fragment implements OnClickListener
{
  private static boolean s_initConnect = true;

  private String m_deviceName;
  private String m_deviceAddress;

  // Default pairing code: 0000
  private static final int REQUEST_ENABLE_BT = 1;
  private static final int REQUEST_OPEN_DEVICE = 2;

  private static String DIDIT_NAME = "DiDiT";

  private static int EMS_MAIN = 80;

  private BluetoothAdapter m_bluetoothAdapter;
  private List<Device> m_deviceList;

  private static Device s_noDevice;

  // private MelodySmartDevice m_melodySmartDevice;

  private TabbedMainActivity getTabbedMainActivity()
  {
    return (TabbedMainActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    View v;

    s_noDevice = new Device("No device found", "invalid");

    // Register for broadcasts when a device is discovered
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    getActivity().registerReceiver(mReceiver, filter);

    v = inflater.inflate(R.layout.fragment_connect, container, false);
    Typeface bold = Fonts.getBold(getActivity().getAssets());
    Typeface regular = Fonts.getRegualr(getActivity().getAssets());
    int colorRed = getResources().getColor(R.color.didit_red_circulair);
    int colorGrey = getResources().getColor(R.color.didit_grey_circulair);

    TextView tv = (TextView) v.findViewById(R.id.connectLabel);
    tv.setTypeface(regular);
    tv.setTextColor(colorGrey);

    tv = (TextView) v.findViewById(R.id.stateConnectLabel);
    tv.setTypeface(regular);
    tv.setTextColor(colorGrey);


    Button button = (Button) v.findViewById(R.id.connectButton);
    button.setOnClickListener(this);

    button = (Button) v.findViewById(R.id.searchButton);
    button.setOnClickListener(this);

    NumberPicker bluetoothPicker;
    bluetoothPicker = (NumberPicker) v.findViewById(R.id.bluetoothNumberPicker);

    final int count = bluetoothPicker.getChildCount();
    try
    {
      for (int i = 0; i < count; i++)
      {
        View child = bluetoothPicker.getChildAt(i);
        if (child instanceof EditText)
        {
          Field[] pickerFields = NumberPicker.class.getDeclaredFields();
          for (Field pf : pickerFields)
          {
            pf.setAccessible(true);
            if (pf.getName().equals("mSelectionDivider"))
            {
              pf.set(bluetoothPicker, null);
            }
            else if (pf.getName().equals("mSelectorWheelPaint"))
            {
              ((Paint) pf.get(bluetoothPicker)).setTextSize(EMS_MAIN);
              ((Paint) pf.get(bluetoothPicker)).setColor(colorRed);
              ((Paint) pf.get(bluetoothPicker)).setTypeface(bold);
              bluetoothPicker.getWidth();

              ((EditText) child).setTextColor(colorRed);
              ((EditText) child).setSelected(false);
              ((EditText) child).setTextSize(27);
              // ((EditText) child).setTextSize(EMS_MAIN);

              ((EditText) child).setTypeface(bold);
              ((EditText) child).setFocusable(false);
            }
          }
        }

        // Only need to invalidate once
        bluetoothPicker.invalidate();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    // volumePicker.setDescendantFocusability(NumberPicker.FOCUS_FORWARD);
    bluetoothPicker
        .setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    m_deviceList = new ArrayList<Device>();

    // m_melodySmartDevice = MelodySmartDevice.getInstance();
    // m_melodySmartDevice.init(getActivity());

    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (m_bluetoothAdapter == null)
    {
      Intent enableBtIntent = new Intent(
          BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      return v;
    }

    if (!m_bluetoothAdapter.isEnabled())
    {
      Intent enableBtIntent = new Intent(
          BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      return v;
    }

    initBluetooth(v);
    refreshList(v);

    return v;
  }

  public void updateConnectState()
  {
    View v;
    v = getView();

    if(v == null)
    {
      return;
    }

    TabbedMainActivity tba;
    tba = getTabbedMainActivity();

    if(tba == null)
    {
      return;
    }

    TextView tv = (TextView) v.findViewById(R.id.stateConnectLabel);
    tv.setText(tba.getConnectMsg());
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    initBluetooth(getView());
    refreshList(getView());
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    // Make sure we're not doing discovery anymore
    if (m_bluetoothAdapter != null)
    {
      m_bluetoothAdapter.cancelDiscovery();
    }

    // Unregister broadcast listeners
    if (mReceiver != null)
    {
      getActivity().unregisterReceiver(mReceiver);
    }
  }

  private void initBluetooth(View v)
  {
    if (v == null)
    {
      v = getView();
      if (v == null)
      {
        return;
      }
    }

    // Check if bluetooth manager is enabled
    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    if ((m_bluetoothAdapter == null) || !m_bluetoothAdapter.isEnabled())
    {
      Toast.makeText(getActivity().getApplicationContext(),
          "Unable to start Bluetooth on device", Toast.LENGTH_SHORT).show();
      return;
    }

    // List all the paired deviced
    Set<BluetoothDevice> pairedDevices = m_bluetoothAdapter.getBondedDevices();
    // If there are paired devices
    if (pairedDevices.size() > 0)
    {
      // Loop through paired devices
      for (BluetoothDevice device : pairedDevices)
      {
        Device d;
        d = new Device(device.getName(), device.getAddress());
        m_deviceList.add(d);
        if (s_initConnect)
        {
          if (StringUtil.contains(device.getName(), DIDIT_NAME))
          {
            connect(d);

          }
        }
      }
    }

    doDiscovery();

    refreshList(v);
  }

  private void refreshList(View v)
  {
    if (v == null)
    {
      v = getView();
      if (v == null)
      {
        return;
      }
    }

    // No paired items
    if (m_deviceList.isEmpty())
    {
      m_deviceList.add(s_noDevice);
    }
    else
    {
      m_deviceList.remove(s_noDevice);
    }

    NumberPicker bluetoothPicker;
    bluetoothPicker = (NumberPicker) v.findViewById(R.id.bluetoothNumberPicker);
    if (bluetoothPicker == null)
    {
      return;
    }

    List<String> nameDeviceList;
    nameDeviceList = new ArrayList<String>();
    for (Device d : m_deviceList)
    {
      nameDeviceList.add(d.getName());
    }

    int maxSize = m_deviceList.size() - 1;
    if (maxSize < 0)
    {
      return;
    }

    String nameArray[] = new String[maxSize];
    nameArray = nameDeviceList.toArray(nameArray);
    bluetoothPicker.setDisplayedValues(nameArray);
    bluetoothPicker.setMinValue(0);
    bluetoothPicker.setMaxValue(maxSize);
    bluetoothPicker.invalidate();

    // requestFocusToButton(v);
  }

  private void requestFocusToButton(View v)
  {
    if (v == null)
    {
      v = getView();
      if (v == null)
      {
        return;
      }
    }

    Button button;
    button = (Button) v.findViewById(R.id.searchButton);
    button.requestFocus();
  }

  /**
   * Start device discover with the BluetoothAdapter
   */
  private void doDiscovery()
  {
    // If we're already discovering, stop it
    if (m_bluetoothAdapter.isDiscovering())
    {
      m_bluetoothAdapter.cancelDiscovery();
    }

    // Request discover from BluetoothAdapter
    m_bluetoothAdapter.startDiscovery();
  }

  @Override
  public void onClick(View v)
  {
    if (v.getId() == R.id.searchButton)
    {
      //ensureDiscoverable();
      doDiscovery();
      return;
    }

    // MelodySmartDevice device;
    int value;
    NumberPicker bluetoothPicker;

    Device d;

    bluetoothPicker = (NumberPicker) getView()
        .findViewById(R.id.bluetoothNumberPicker);
    value = bluetoothPicker.getValue();

    if ((m_deviceList == null) || (m_deviceList.size() == 0))
    {
      return;
    }

    d = m_deviceList.get(value);
    connect(d);

    // startActivity(intent);
    // finish();
  }

  private void connect(Device d)
  {
    if ((m_bluetoothAdapter == null) || !m_bluetoothAdapter.isEnabled())
    {
      initBluetooth(getView());
    }

    s_initConnect = false;
    if (d == null)
    {
      return;
    }

    m_deviceName = d.getName();
    m_deviceAddress = d.getAddress();

    if (StringUtil.isEmpty(m_deviceName) || StringUtil.isEmpty(m_deviceAddress))
    {
      return;
    }

    getTabbedMainActivity().connect(m_deviceName, m_deviceAddress);
    getTabbedMainActivity().gotoNextPage();
  }

  private void ensureDiscoverable()
  {
    if(m_bluetoothAdapter == null)
    {
      return;
    }

    if (m_bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
      Intent discoverableIntent = new Intent(
          BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
      discoverableIntent.putExtra(
          BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
      startActivity(discoverableIntent);
    }
  }

  public String getDeviceAddress()
  {
    return m_deviceAddress;
  }

  public String getDeviceName()
  {
    return m_deviceName;
  }

  private class Device
  {
    private String mi_name;
    private String mi_address;

    private Device(String name, String address)
    {
      mi_name = name;
      mi_address = address;
    }

    private String getName()
    {
      return mi_name;
    }

    private String getAddress()
    {
      return mi_address;
    }
  }

  // The BroadcastReceiver that listens for discovered devices and
  // changes the title when discovery is finished
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    @Override
    public void onReceive(Context context, Intent intent)
    {
      String action = intent.getAction();

      // When discovery finds a device
      if (BluetoothDevice.ACTION_FOUND.equals(action))
      {
        // Get the BluetoothDevice object from the Intent
        BluetoothDevice device = intent
            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        // If it's already paired, skip it, because it's been listed already
        if (device.getBondState() != BluetoothDevice.BOND_BONDED)
        {
          if (StringUtil.isEmpty(device.getName())
              || StringUtil.isEmpty(device.getAddress()))
          {
            return;
          }

          for (Device d : m_deviceList)
          {
            if (StringUtil.isEqual(d.getName(), device.getName()))
            {
              return;
            }
          }

          m_deviceList.add(new Device(device.getName(), device.getAddress()));
          refreshList(getView());
        }
        // When discovery is finished, change the Activity title
      }
      else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
      {
        Toast.makeText(getActivity().getApplicationContext(),
            "Finished searching BT Devices", Toast.LENGTH_LONG).show();

        // Done scanning
      }
    }
  };

}
