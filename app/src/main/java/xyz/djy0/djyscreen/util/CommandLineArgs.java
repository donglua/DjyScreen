package xyz.djy0.djyscreen.util;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/14 下午3:54
 */

public class CommandLineArgs {

  public static Hashtable<String, String> parse(String[] array) {
    final Hashtable<String, String> hashtable = new Hashtable<>();
    for (int length = array.length, i = 0; i < length; ++i) {
      final String[] split = array[i].split("=", 2);
      String s = "";
      if (split.length == 2) {
        s = split[1];
      }
      hashtable.put(split[0], s);
    }

    return hashtable;
  }
}
