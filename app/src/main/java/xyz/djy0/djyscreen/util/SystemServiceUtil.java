package xyz.djy0.djyscreen.util;

import android.hardware.display.IDisplayManager;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.IPowerManager;
import android.view.DisplayInfo;
import android.view.IWindowManager;
import java.lang.reflect.Method;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/14 下午4:39
 */

public class SystemServiceUtil {

  public static IWindowManager getWindowManager() throws Exception {
    final Method declaredMethod =
        Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
    final IWindowManager windowManager =
        IWindowManager.Stub.asInterface((IBinder) declaredMethod.invoke(null, "window"));
    return windowManager;
  }

  public static IPowerManager getPowerManager() throws Exception {
    final Method declaredMethod =
        Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
    return IPowerManager.Stub.asInterface((IBinder) declaredMethod.invoke(null, "power"));
  }

  public static DisplayInfo getDisplayInfo() throws Exception {
    final Method declaredMethod =
        Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
    final DisplayInfo displayInfo =
        IDisplayManager.Stub.asInterface((IBinder) declaredMethod.invoke(null, "display")).getDisplayInfo(0);
    return displayInfo;
  }

  public static InputManager getInputManager() {
    return (InputManager) ReflectionUtils.invokeMethod(InputManager.class, "getInstance", null, null);
  }
}
