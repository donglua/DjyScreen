package xyz.djy0.djyscreen.util;

import android.hardware.input.InputManager;
import android.os.IPowerManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import java.lang.reflect.Method;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 上午11:46
 */

public class AndroidDeviceUtils {
  public static void turnScreenOn() {
    IPowerManager powerManager = null;
    try {
      powerManager = SystemServiceUtil.getPowerManager();
      if (!powerManager.isScreenOn()) {
        sendKeyEvent(SystemServiceUtil.getInputManager(), 257, 26, false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void sendKeyEvent(final InputManager inputManager, final int source, final int code, final boolean b) {
    final long uptimeMillis = SystemClock.uptimeMillis();
    injectKeyEvent(inputManager, new KeyEvent(uptimeMillis, uptimeMillis, 0, code, 0, (b ? 1 : 0), -1, 0, 0, source));
    injectKeyEvent(inputManager, new KeyEvent(uptimeMillis, uptimeMillis, 1, code, 0, (b ? 1 : 0), -1, 0, 0, source));
  }

  private static void injectKeyEvent(InputManager inputManager, KeyEvent keyEvent) {
    Class[] classes = { KeyEvent.class, int.class };
    Object[] objects = { keyEvent, 0 };
    ReflectionUtils.invokeMethod(inputManager, ReflectConst.Method.INPUT_MANAGER_injectInputEvent, classes, objects);
  }
}
