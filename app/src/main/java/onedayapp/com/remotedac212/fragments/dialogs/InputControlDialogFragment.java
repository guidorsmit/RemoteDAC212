package onedayapp.com.remotedac212.fragments.dialogs;

import onedayapp.com.remotedac212.*;
import onedayapp.com.remotedac212.fragments.*;
import onedayapp.com.remotedac212.settings.*;
import onedayapp.com.remotedac212.states.*;
import onedayapp.com.utils.*;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class InputControlDialogFragment extends DialogFragment
    implements OnClickListener
{
  private InputControl m_inputControl;
  private MainFragment m_mainFragment;

  public InputControlDialogFragment(InputControl inputControl,
      MainFragment mainFragment)
  {
    m_inputControl = inputControl;
    m_mainFragment = mainFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
  {
    RadioButton rbutton;
    Typeface regular;
    int colorRed;

    regular = Fonts.getRegualr(getActivity().getAssets());
    colorRed = getResources().getColor(R.color.didit_red_circulair);

    View v = inflater.inflate(R.layout.dialog_fragment_input_control, container,
        false);
    // 1
    rbutton = (RadioButton) v.findViewById(R.id.radioButton1);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.XLR.getName());

    if (m_inputControl == InputControl.XLR)
    {
      rbutton.setChecked(true);
    }

    // 2
    rbutton = (RadioButton) v.findViewById(R.id.radioButton2);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.RCA1.getName());

    if (m_inputControl == InputControl.RCA1)
    {
      rbutton.setChecked(true);
    }

    // 3
    rbutton = (RadioButton) v.findViewById(R.id.radioButton3);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.RCA2.getName());

    if (m_inputControl == InputControl.RCA2)
    {
      rbutton.setChecked(true);
    }

    // 4
    rbutton = (RadioButton) v.findViewById(R.id.radioButton4);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.OPTICAL.getName());

    if (m_inputControl == InputControl.OPTICAL)
    {
      rbutton.setChecked(true);
    }

    // 5
    rbutton = (RadioButton) v.findViewById(R.id.radioButton5);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.USB.getName());

    if (m_inputControl == InputControl.USB)
    {
      rbutton.setChecked(true);
    }

    // 6
    rbutton = (RadioButton) v.findViewById(R.id.radioButton6);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.BT.getName());

    if (m_inputControl == InputControl.BT)
    {
      rbutton.setChecked(true);
    }

    // 7
    rbutton = (RadioButton) v.findViewById(R.id.radioButton7);
    rbutton.setOnClickListener(this);
    rbutton.setTypeface(regular);
    rbutton.setTextColor(colorRed);
    rbutton.setText(InputControl.IC2_1.getName());

    if (m_inputControl == InputControl.IC2_1)
    {
      rbutton.setChecked(true);
    }


    return v;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    // The only reason you might override this method when using
    // onCreateView()
    // is
    // to modify any dialog characteristics. For example, the dialog includes
    // a
    // title by default, but your custom layout might not need it. So here you
    // can
    // remove the dialog title, but you must call the superclass to get the
    // Dialog.
    Dialog dialog;

    dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_fragment_input_control);

    return dialog;
  }

  @Override
  public void onClick(View v1)
  {
    int id;
    InputControl inputControl;
    id = v1.getId();
    if (id == R.id.radioButton1)
    {
      inputControl = InputControl.XLR;
    }
    else if (id == R.id.radioButton2)
    {
      inputControl = InputControl.RCA1;
    }
    else if (id == R.id.radioButton3)
    {
      inputControl = InputControl.RCA2;
    }
    else if (id == R.id.radioButton4)
    {
      inputControl = InputControl.OPTICAL;
    }
    else if (id == R.id.radioButton5)
    {
      inputControl = InputControl.USB;
    }
    else if (id == R.id.radioButton6)
    {
      inputControl = InputControl.BT;
    }
    else if (id == R.id.radioButton7)
    {
      inputControl = InputControl.IC2_1;
    }
    else
    {
      System.out.println("Undefined type");
      return;
    }

    if (m_mainFragment != null)
    {
      m_mainFragment.updateInputControl(inputControl, true);
    }

    dismiss();
  }

  private void displayError(String error)
  {
    if (StringUtil.isEmpty(error))
    {
      return;
    }
    // display in long period of time
    Toast.makeText(getActivity().getApplicationContext(), error,
        Toast.LENGTH_LONG).show();
  }
}
