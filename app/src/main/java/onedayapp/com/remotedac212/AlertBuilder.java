package onedayapp.com.remotedac212;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;

/**
 * Created by genis on 21/04/2015.
 */
public class AlertBuilder extends AlertDialog.Builder
{

  private Context mContext;

  public AlertBuilder(Context context)
  {
    super(context);
    mContext = context;
  }

  public AlertDialog.Builder setMessage(String message)
  {
    TextView textView = new TextView(mContext);
    textView.setText(message);
    textView.setGravity(Gravity.CENTER_HORIZONTAL);
    textView.setPadding(25, 20, 25, 20);
    return super.setView(textView);
  }
}
