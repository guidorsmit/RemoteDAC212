package onedayapp.com.utils;

import java.text.*;
import java.util.*;

/**
 * DateUtil, handles everything concerning date related operations
 * 
 * @author GRS
 * 
 */
public class DateUtil
{
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String TIME_FORMAT = "HH:mm:ss";
  private static final String DATE_FILE_FORMAT = "yyyy_MM_dd_HH_mm";

  /**
   * Get the date {@link String}
   * 
   * @return {@link String} in format yyyy-MM-dd
   */
  public static String getDateString()
  {
    return getDate(DATE_FORMAT);
  }

  /**
   * Get the date file format {@link String}
   * 
   * @return {@link String} in format yyyy-MM-dd
   */
  public static String getDateFileFormatString()
  {
    return getDate(DATE_FILE_FORMAT);
  }

  /**
   * Get the time {@link String}
   * 
   * @return {@link String} in format HH:mm:ss
   */
  public static String getTimeString()
  {
    return getDate(TIME_FORMAT);
  }

  private static String getDate(String type)
  {
    Calendar cal;
    SimpleDateFormat sdf;
    cal = Calendar.getInstance();
    sdf = new SimpleDateFormat(type);
    return sdf.format(cal.getTime());
  }

  /**
   * Parse a time string to a representive {@link Date} object
   * 
   * @param {@link String}time
   * @return {@link Date} converted string2date
   */
  public static Date parseString2Date(String time)
  {
    SimpleDateFormat sdf;
    sdf = new SimpleDateFormat(DATE_FORMAT);

    try
    {
      return sdf.parse(time);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    return new Date();
  }
}
