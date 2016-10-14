package xyz.djy0.djyscreen;

import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Looper;
import android.util.Log;
import android.view.IWindowManager;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Hashtable;
import xyz.djy0.djyscreen.util.CommandLineArgs;
import xyz.djy0.djyscreen.util.L;
import xyz.djy0.djyscreen.util.SystemServiceUtil;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/14 下午3:42
 */

public class Main {
  private static final String TAG = Main.class.getSimpleName();
  private static double resolution;

  private static AsyncServer server;
  private static String commandLinePassword;

  static Looper looper;

  static {
    Main.resolution = 0.0;
    Main.server = new AsyncServer();
  }

  public static void main(String[] array) {
    try {

      L.d(TAG, "launch main");

      final IPowerManager powerManager = SystemServiceUtil.getPowerManager();
      final IWindowManager windowManager = SystemServiceUtil.getWindowManager();

      AsyncHttpServer httpServer = new AsyncHttpServer() {
        protected boolean onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
          return super.onRequest(request, response);
        }
      };

      Looper.prepare();
      looper = Looper.myLooper();

      httpServer.get("/screenshot.jpg", new HttpServerRequestCallback() {
        @Override
        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
          try {
            L.d(TAG, "start request");
            Bitmap bitmap = ScreenShotFb.screenshot(windowManager);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);
            bout.flush();
            response.send("image/jpeg", bout.toByteArray());
            return;
          } catch (Exception e) {
            response.code(500);
            response.send(e.toString());
            return;
          }
        }
      });
      httpServer.listen(server, 53516);
      L.d(TAG, powerManager.toString());
      L.d(TAG, windowManager.toString());
      L.d(TAG, httpServer.toString());
      Looper.loop();
    } catch (Exception e) {
      L.d(TAG, e.toString());
    }
  }
}
