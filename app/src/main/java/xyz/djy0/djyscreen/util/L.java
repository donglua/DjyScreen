package xyz.djy0.djyscreen.util;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/14 下午5:34
 */

public class L {
  public static void d(String tag, String message) {
    System.out.println(tag + "  : " + message);
  }

  public static void d(String tag, String message, Throwable other) {
    System.out.println(tag + "  : " + message + "  " + other.toString());
  }
}
