package onedayapp.com.remotedac212.fragments.dialogs;

import onedayapp.com.remotedac212.*;
import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.settings.*;

import android.annotation.SuppressLint;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;

@SuppressLint("ValidFragment")
public class QuickSettingsDialogFragment extends DialogFragment
    implements OnCheckedChangeListener
{
  private MainFragment m_mainFragment;

  public QuickSettingsDialogFragment(MainFragment mainFragment)
  {
    m_mainFragment = mainFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    Switch wSwitch;
    Typeface regular;
    int colorRed;

    regular = Fonts.getRegualr(getActivity().getAssets());
    colorRed = getResources().getColor(R.color.didit_red_circulair);

    View v = inflater.inflate(R.layout.dialog_fragment_quicksettings, container,
        false);

    wSwitch = (Switch) v
        .findViewById(R.id.switch_quicksettings_deemphasis_filter);
    wSwitch.setOnCheckedChangeListener(this);
    wSwitch = (Switch) v.findViewById(R.id.switch_quicksettings_fir_roll);
    wSwitch.setOnCheckedChangeListener(this);
    wSwitch = (Switch) v.findViewById(R.id.switch_quicksettings_streaming);
    wSwitch.setOnCheckedChangeListener(this);
    wSwitch = (Switch) v.findViewById(R.id.switch_quicksettings_osf_filter);
    wSwitch.setOnCheckedChangeListener(this);

    return v;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
  {

  }
}
