package xyz.djy0.djyscreen.util;

import android.os.Looper;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 上午11:51
 */

public class ServiceLooper {

  private static Looper looper;

  public static void prepare() {
    if (looper == null) {
      Looper.prepare();
    }
    looper = Looper.myLooper();
  }
}
