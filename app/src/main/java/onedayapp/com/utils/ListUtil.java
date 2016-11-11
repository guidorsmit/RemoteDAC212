package onedayapp.com.utils;

import java.io.*;
import java.util.*;

public class ListUtil implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * get the index number of the compare item<br>
   * Helper method
   * 
   * @param {@link ArrayList} list
   * @param {@link String} compare
   * 
   * @return {@link Integer} index number; if item was nof found return -1;
   */
  public static int getIndexOfItem(List<? extends Comparable<?>> list,
      Object compare)
  {
    for (Object item : list)
    {
      if (item.equals(compare))
      {
        return list.indexOf(item);
      }
    }
    return -1;
  }

}
