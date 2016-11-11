package onedayapp.com.remotedac212.states;

import java.util.*;

import onedayapp.com.utils.*;

public enum InputControl
{
  // ANALOG("anaglog"), DIGITAL("digital"), USB("usb");
  XLR("AES", 1), RCA1("COAX 1", 2), RCA2("COAX 2", 3), OPTICAL("OPTICAL",
      4), USB("USB", 5), BT("Bluetooth", 6), IC2_1("I2S", 7), IC2_2("I2S#2",
          8), IC2_3("I2S#3", 9), IC2_4("I2S#4", 10);

  private String m_name;
  private int m_state;

  private InputControl(String name, int state)
  {
    m_name = name;
    m_state = state;
  }

  protected void setName(String name)
  {
    m_name = name;
  }

  public int getState()
  {
    return m_state;
  }

  public String getName()
  {
    return StringUtil.toUpperCase(m_name);
  }

  public static List<InputControl> getInputControlList()
  {
    List<InputControl> list;

    list = new ArrayList<>();
    list.add(XLR);
    list.add(RCA1);
    list.add(RCA2);
    list.add(OPTICAL);
    list.add(USB);
    list.add(BT);
    list.add(IC2_1);
    list.add(IC2_2);
    list.add(IC2_3);
    list.add(IC2_4);

    return list;
  }
}
