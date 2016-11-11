package onedayapp.com.remotedac212.settings;

import android.content.res.*;
import android.graphics.*;

public class Fonts
{
  public static Typeface getRegualr(AssetManager am)
  {
    return Typeface.createFromAsset(am, "fonts/lato_regular.ttf");
  }

  public static Typeface getBold(AssetManager am)
  {
    return Typeface.createFromAsset(am, "fonts/lato_bold.ttf");
  }

}
