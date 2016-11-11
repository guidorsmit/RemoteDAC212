package onedayapp.com.remotedac212.widgets;

import onedayapp.com.remotedac212.fragments.*;

import android.support.v4.app.*;

public class PagerAdapter extends FragmentStatePagerAdapter
{
  int mNumOfTabs;
  ConnectFragment tab1;
  MainFragment tab2;
  SettingsDisplayFragment tab3;

  public PagerAdapter(FragmentManager fm, int NumOfTabs)
  {
    super(fm);
    this.mNumOfTabs = NumOfTabs;

    tab1 = new ConnectFragment();
    tab2 = new MainFragment();
    tab3 = new SettingsDisplayFragment();
  }

  @Override
  public Fragment getItem(int position)
  {

    switch (position)
    {
    case 0:
      return tab1;
    case 1:
      return tab2;
    case 2:
      return tab3;
    default:
      return null;
    }
  }

  @Override
  public int getCount()
  {
    return mNumOfTabs;
  }

  public ConnectFragment getConnectFragment()
  {
    return tab1;
  }
}