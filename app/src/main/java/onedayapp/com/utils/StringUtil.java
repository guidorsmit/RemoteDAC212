package onedayapp.com.utils;

import java.util.*;

import android.annotation.*;

public class StringUtil
{

  /**
   * Compare left string to right string
   * 
   * @param {@Link
   *          String} s1
   * @param {@Link
   *          String} s2
   * @return
   */
  @SuppressLint("NewApi")
  public static boolean isEqual(String s1, String s2)
  {
    if ((s1 == null) && (s2 == null))
    {
      return true;
    }

    if (s1.isEmpty() && s2.isEmpty())
    {
      return true;
    }

    if (s1.equals(s2))
    {
      return true;
    }

    return false;
  }

  public static String getEmptyString()
  {
    return new String("");
  }

  public static boolean startsWith(String s1, String prefix)
  {
    if ((s1 == null) || (prefix == null))
    {
      return false;
    }

    if (s1.isEmpty() || prefix.isEmpty())
    {
      return false;
    }

    return s1.startsWith(prefix, 0);
  }

  public static boolean endsWith(String s1, String postfix)
  {
    if ((s1 == null) || (postfix == null))
    {
      return false;
    }

    if (s1.isEmpty() || postfix.isEmpty())
    {
      return false;
    }

    return s1.endsWith(postfix);
  }

  public static List<String> split(String s1, String regex)
  {
    String array[];

    if ((s1 == null) || s1.isEmpty())
    {
      return new ArrayList<String>();
    }

    if ((regex == null) || regex.isEmpty())
    {
      return new ArrayList<String>();
    }

    array = s1.split(regex);
    return new ArrayList<String>(Arrays.asList(array));
  }

  public static boolean contains(String s1, String contains)
  {
    if ((s1 == null) && (contains == null))
    {
      return true;
    }

    if (s1.isEmpty() && contains.isEmpty())
    {
      return true;
    }

    if (s1.contains(contains))
    {
      return true;
    }

    return false;
  }

  public static boolean isEmpty(String s)
  {
    if ((s == null) || s.isEmpty())
    {
      return true;
    }

    return false;
  }

  public static String toUpperCase(String conv)
  {
    if (isEmpty(conv))
    {
      return "";
    }

    return conv.toUpperCase();
  }
}
