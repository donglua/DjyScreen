package xyz.djy0.djyscreen;

import android.graphics.Bitmap;
import com.koushikdutta.async.BufferedDataSink;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import java.io.ByteArrayOutputStream;
import xyz.djy0.djyscreen.util.AndroidDeviceUtils;
import xyz.djy0.djyscreen.util.L;
import com.koushikdutta.virtualdisplay.StdOutDevice;

import static android.content.ContentValues.TAG;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 上午11:41
 */

class WebServices {

  static void registerAllServices(AsyncHttpServer httpServer) {
    registerH264(httpServer);
    registerScreenshot(httpServer);
  }

  private static void registerScreenshot(AsyncHttpServer httpServer) {
    httpServer.get("/screenshot.jpg", new HttpServerRequestCallback() {
      @Override
      public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        try {
          L.d(TAG, "start request");
          long startTime = System.currentTimeMillis();
          Bitmap bitmap = ScreenShotFb.screenshot();
          ByteArrayOutputStream bout = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);
          bout.flush();
          response.send("image/jpeg", bout.toByteArray());
          long endTime = System.currentTimeMillis();
          L.d(TAG, "response time=" + (endTime - startTime));
        } catch (Exception e) {
          response.code(500);
          response.send(e.toString());
        }
      }
    });
  }

  private static void registerH264(AsyncHttpServer httpServer) {
    httpServer.get("/h264", new HttpServerRequestCallback() {
      @Override
      public void onRequest(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
        AndroidDeviceUtils.turnScreenOn();
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Connection", "close");
        response.setClosedCallback(new CompletedCallback() {
          StdOutDevice device = StdOutDevice.genStdOutDevice(new BufferedDataSink(response));

          @Override
          public void onCompleted(Exception ex) {
            device.stop();
          }
        });
      }
    });
  }
}
