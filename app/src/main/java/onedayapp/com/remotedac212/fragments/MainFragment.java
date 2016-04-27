package onedayapp.com.remotedac212.fragments;

import java.lang.reflect.*;

import onedayapp.com.remotedac212.*;
import onedayapp.com.remotedac212.datacallback.*;
import onedayapp.com.remotedac212.fragments.dialogs.*;
import onedayapp.com.remotedac212.settings.*;
import onedayapp.com.remotedac212.states.*;
import onedayapp.com.remotedac212.widgets.*;

import android.content.*;
import android.database.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.NumberPicker.*;

public class MainFragment extends Fragment
    implements OnClickListener, OnValueChangeListener
{
  private ProgressCircle m_progressCircle;

  // V2
  float m_currentVolume;

  private static int EMS_LABELS = 46;
  private static int EMS_MAIN = 80;

  private static int COLOR_DIDIT_RED;
  private static int COLOR_DIDIT_GREY;
  private static int COLOR_DIDIT_GREY_DARK;
  private static int COLOR_DIDIT_GREY_LIGHT;

  private PowerState m_powerState;
  private MuteState m_muteState;
  private InputControl m_inputControl;

  private boolean m_sendVolume = true;

  // Volume observer registering callbac to volume up volume down
  private VolumeContentObserver m_volumeContentObserver; // REVIEW guido do we
                                                         // want to keep this

  private TabbedMainActivity getTabbedMainActivity()
  {
    return (TabbedMainActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    View v;

    v = inflater.inflate(R.layout.fragment_main, container, false);

    NumberPicker volumePicker;
    Button button;
    TextView tv;
    DataContainer dataContainer;
    AudioManager audio;

    Typeface bold;
    Typeface regular;

    String cmd;
    String prefix;

    // Collect colors
    COLOR_DIDIT_GREY = getResources().getColor(R.color.didit_grey_circulair);
    COLOR_DIDIT_GREY_DARK = getResources()
        .getColor(R.color.didit_grey_selected_circulair);
    COLOR_DIDIT_GREY_LIGHT = getResources()
        .getColor(R.color.didit_light_grey_circulair);
    COLOR_DIDIT_RED = getResources().getColor(R.color.didit_red_circulair);

    // Create fonts
    bold = Fonts.getBold(getActivity().getAssets());
    regular = Fonts.getRegualr(getActivity().getAssets());

    button = (Button) v.findViewById(R.id.inputButton);
    button.setOnClickListener(this);

    button = (Button) v.findViewById(R.id.inputButtonSettings);
    button.setOnClickListener(this);

    button = (Button) v.findViewById(R.id.muteButton);
    button.setOnClickListener(this);

    button = (Button) v.findViewById(R.id.powerButton);
    button.setOnClickListener(this);

    audio = (AudioManager) getActivity().getApplicationContext()
        .getSystemService(Context.AUDIO_SERVICE);
    m_currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

    /*
     * m_volumeContentObserver = new VolumeContentObserver(new Handler());
     * getActivity().getApplicationContext().getContentResolver()
     * .registerContentObserver(android.provider.Settings.System.CONTENT_URI,
     * true, m_volumeContentObserver);
     */
    // Update the current value in view
    volumePicker = (NumberPicker) v.findViewById(R.id.volumeNumberPicker);
    updateVolume(v);
    // volumePicker.setValue((int) m_currentVolume);
    volumePicker.setWrapSelectorWheel(false);

    m_progressCircle = (ProgressCircle) v.findViewById(R.id.progress_circle);

    m_progressCircle.setProgress(0.0f);
    m_progressCircle.startAnimation();

    // Assign fonts + color

    button = (Button) v.findViewById(R.id.inputButton);
    button.setTypeface(bold);
    // tv.setEms(EMS_MAIN);
    button.setTextColor(COLOR_DIDIT_RED);
    button.setBackgroundColor(Color.TRANSPARENT);

    tv = (TextView) v.findViewById(R.id.bpmLabel);
    tv.setTypeface(regular);
    // tv.setEms(EMS_LABELS);
    tv.setTextColor(COLOR_DIDIT_GREY);

    cmd = getResources().getString(R.string.get_channel_word_length_prefix);
    prefix = getResources().getString(R.string.channel_word_length_prefix);
    dataContainer = new DataContainerChannelWordLength(tv, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dataContainer);

    tv = (TextView) v.findViewById(R.id.bitrateLabel);
    tv.setTypeface(regular);
    // tv.setEms(EMS_LABELS);
    tv.setTextColor(COLOR_DIDIT_GREY);

    cmd = getResources().getString(R.string.get_channel_freq_prefix);
    prefix = getResources().getString(R.string.channel_freq_prefix);
    dataContainer = new DataContainerChannelInputFrequency(tv, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dataContainer);

    tv = (TextView) v.findViewById(R.id.baudrateLabel);
    tv.setTypeface(regular);
    // tv.setEms(EMS_LABELS);
    tv.setTextColor(COLOR_DIDIT_GREY);

    cmd = getResources().getString(R.string.get_baudrate_prefix);
    prefix = getResources().getString(R.string.baudrate_prefix);
    dataContainer = new DataContainer(tv, cmd, prefix);
    // getTabbedMainActivity().addDataCallback(dataContainer);

    // Add volume handler
    cmd = getResources().getString(R.string.get_volume_prefix);
    prefix = getResources().getString(R.string.volume_prefix);
    DataContainerVolume dcv = new DataContainerVolume(this, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dcv);

    // Add input handler
    cmd = getResources().getString(R.string.get_input_prefix);
    prefix = getResources().getString(R.string.input_prefix);
    DataContainerInput dci = new DataContainerInput(this, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dci);

    // Add power handler
    cmd = getResources().getString(R.string.get_power_prefix);
    prefix = getResources().getString(R.string.power_prefix);
    DataContainerPower dcp = new DataContainerPower(this, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dcp);

    // Add mute handler
    cmd = getResources().getString(R.string.get_mute_prefix);
    prefix = getResources().getString(R.string.mute_prefix);
    DataContainerMute dcm = new DataContainerMute(this, cmd, prefix);
    getTabbedMainActivity().addDataCallback(dcm);

    return v;
  }

  public void updateVolume(View v)
  {
    NumberPicker volumePicker;

    if (v == null)
    {
      v = getView();
    }

    volumePicker = (NumberPicker) v.findViewById(R.id.volumeNumberPicker);
    volumePicker.setOnValueChangedListener(this);

    // TODO collect max and min volume
    VolumeHolder.setMaxMaxVolume(0);
    VolumeHolder.setMaxMinVolume(-60);

    volumePicker.setMinValue(0);
    volumePicker.setMaxValue(
        VolumeHolder.getMaxMaxVolume() + (VolumeHolder.getMaxMinVolume() * -1));

    // volumePicker.setDescendantFocusability(NumberPicker.FOCUS_FORWARD);
    volumePicker
        .setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    volumePicker.setFormatter(new NumberPicker.Formatter()
    {
      @Override
      public String format(int index)
      {
        // System.out.println(
        // "index " + index + " " + (VolumeHolder.getMaxMinVolume() + index));
        return Integer.toString(VolumeHolder.getMaxMinVolume() + index);
      }
    });

    final int count = volumePicker.getChildCount();
    try
    {
      for (int i = 0; i < count; i++)
      {
        View child = volumePicker.getChildAt(i);
        if (child instanceof EditText)
        {
          Field[] pickerFields = NumberPicker.class.getDeclaredFields();
          for (Field pf : pickerFields)
          {
            if (pf.getName().equals("mSelectionDivider"))
            {
              pf.setAccessible(true);
              pf.set(volumePicker, null);
            }
            else if (pf.getName().equals("mSelectorWheelPaint"))
            {
              Typeface bold;
              bold = Fonts.getBold(getActivity().getAssets());

              pf.setAccessible(true);
              ((Paint) pf.get(volumePicker)).setTextSize(EMS_MAIN);
              ((Paint) pf.get(volumePicker)).setColor(COLOR_DIDIT_RED);
              ((Paint) pf.get(volumePicker)).setTypeface(bold);

              ((EditText) child).setTextColor(COLOR_DIDIT_RED);
              ((EditText) child).setSelected(false);
              // ((EditText) child).setEms(EMS_MAIN);
              ((EditText) child).setTypeface(bold);
              ((EditText) child).setTextSize(27);
            }
          }
        }
        // Only need to invalidate once
        volumePicker.invalidate();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    // volumePicker.setDescendantFocusability(NumberPicker.FOCUS_FORWARD);
    volumePicker
        .setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    volumePicker.setWrapSelectorWheel(false);
  }

  public void setCurrentVolume(int volume)
  {
    // Update the current value in view
    NumberPicker volumePicker = (NumberPicker) getView()
        .findViewById(R.id.volumeNumberPicker);

    int setVolume = (VolumeHolder.getMaxMinVolume() * -1) - volume;

    m_sendVolume = false;
    updateVolume(null);
    volumePicker.setValue(setVolume);
    volumePicker.setWrapSelectorWheel(false);
    animate(setVolume);

    // m_currentVolume = volume;
  }

  public void animate(int newVal)
  {
    // Max value 0.66
    // float maxValue = 2.0f / 3.0f;
    float maxValue = 0.627f;
    float range = VolumeHolder.getMaxMaxVolume()
        - VolumeHolder.getMaxMinVolume();

    m_currentVolume = (newVal / range) * maxValue;

    if (m_progressCircle == null)
    {
      return;
    }

    // System.out.println("tmp: curent" + m_currentVolume);
    m_progressCircle.setProgress(m_currentVolume);
    m_progressCircle.startAnimation();
  }

  @Override
  public void onDestroy()
  {
    // Remove volume content observer
    // getActivity().getApplicationContext().getContentResolver()
    // .unregisterContentObserver(m_volumeContentObserver);
    super.onDestroy();
  }

  @Override
  public void onValueChange(NumberPicker picker, int oldVal, int newVal)
  {
    int id;

    if (oldVal == newVal)
    {
      return;
    }

    id = picker.getId();
    if (id == R.id.volumeNumberPicker)
    {
      // int delta;
      // int volume;

      // Caluclate delta
      // delta = oldVal - newVal;
      // if (delta < 0)
      // {
      // delta *= -1;
      // }

      // Delta is number of steps
      // if (newVal > oldVal)
      // {
      // volume = 1;
      // }
      // else
      // {
      // Volume down
      // volume = -1;
      // }

      // for (int i = 0; i < delta; i++)
      // {
      // Volume up
      // getTabbedMainActivity().sendVolume(volume);
      // }

      // sendVolume prefents infinite loop
      if (m_sendVolume)
      {
        int volumeSend = (VolumeHolder.getMaxMinVolume() + newVal) * -1;
        getTabbedMainActivity().sendAbsoluteVolume(volumeSend);
      }
      m_sendVolume = true;

      // m_currentVolume = (1.0f - ((newVal / range) * -1.0f)) * maxValue;
      // m_currentVolume = ((newVal * 1.0f * 2.0f) / 100.0f) * (2.0f / 3.0f);
      // System.out.println("currentVal: " + m_currentVolume + " : " + newVal);
      animate(newVal);
    }
    else
    {
      System.out.println("Strange id not regoniced");
    }
  }

  @Override
  public void onClick(View v)
  {
    int id;

    id = v.getId();
    if (id == R.id.inputButton)
    {
      InputControlDialogFragment icdf;
      icdf = new InputControlDialogFragment(m_inputControl, this);
      icdf.show(getActivity().getSupportFragmentManager(), "InputControl");
      return;
    }
    else if (id == R.id.inputButtonSettings)
    {
      QuickSettingsDialogFragment qsdf;
      qsdf = new QuickSettingsDialogFragment(this);
      qsdf.show(getActivity().getSupportFragmentManager(), "QuickSettings");
      return;
    }
    else if (id == R.id.powerButton)
    {
      if (m_powerState == PowerState.ON)
      {
        updatePowerState(PowerState.OFF);
      }
      else if (m_powerState == PowerState.OFF)
      {
        updatePowerState(PowerState.ON);
      }
      getTabbedMainActivity().sendPower(m_powerState);
      return;
    }
    else if (id == R.id.muteButton)
    {
      if (m_muteState == MuteState.OFF)
      {
        updateMuteState(MuteState.ON);
      }
      else if (m_muteState == MuteState.ON)
      {
        updateMuteState(MuteState.OFF);
      }
      getTabbedMainActivity().sendMute(m_muteState);
      return;
    }
  }

  public void updatePowerState(PowerState powerState)
  {
    Button button;
    button = (Button) getView().findViewById(R.id.powerButton);

    if (button == null)
    {
      return;
    }

    if (powerState == null)
    {
      return;
    }

    m_powerState = powerState;
    if (m_powerState == PowerState.ON)
    {
      button.setBackgroundResource(R.drawable.button_power_on_wrapper);
    }
    else if (m_powerState == PowerState.OFF)
    {
      button.setBackgroundResource(R.drawable.button_power_off_wrapper);
    }
  }

  public void updateMuteState(MuteState muteState)
  {
    Button button;
    button = (Button) getView().findViewById(R.id.muteButton);

    if (button == null)
    {
      return;
    }

    if (muteState == null)
    {
      return;
    }

    m_muteState = muteState;
    if (m_muteState == MuteState.ON)
    {
      button.setBackgroundResource(R.drawable.button_mute_on_wrapper);
    }
    else if (m_muteState == MuteState.OFF)
    {
      button.setBackgroundResource(R.drawable.button_mute_off_wrapper);
    }
  }

  public void updateInputControl(InputControl inputControl, boolean toSend)
  {
    Button button;
    String text;

    text = "USB";
    m_inputControl = inputControl;
    text = m_inputControl.getName();

    button = (Button) getView().findViewById(R.id.inputButton);
    if (button != null)
    {
      button.setText(text);
    }

    if (toSend)
    {
      getTabbedMainActivity().sendInputControl(m_inputControl);
    }
  }

  /**
   * VolumeContentObserver
   * 
   * Detects all volume input changes
   * 
   * @author GRS
   * 
   */
  public class VolumeContentObserver extends ContentObserver
  {
    private Context mi_context;

    public VolumeContentObserver(Handler handler)
    {
      super(handler);
      mi_context = getActivity().getApplicationContext();

      AudioManager audio = (AudioManager) mi_context
          .getSystemService(Context.AUDIO_SERVICE);
      m_currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications()
    {
      return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange)
    {
      super.onChange(selfChange);

      AudioManager audio = (AudioManager) mi_context
          .getSystemService(Context.AUDIO_SERVICE);
      float currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

      System.out.println("volume" + currentVolume);
      // int delta = (int) (m_currentVolume - currentVolume);
      int delta = 0;
      if (delta > 0)
      {
        m_currentVolume = currentVolume;
        for (int i = 0; i < delta; i++)
        {
          getTabbedMainActivity().sendVolume(1);
        }
      }
      else if (delta < 0)
      {
        m_currentVolume = currentVolume;
        for (int i = 0; i > delta; i--)
        {
          getTabbedMainActivity().sendVolume(-1);
        }
      }

      // animate();
    }
  }
}
