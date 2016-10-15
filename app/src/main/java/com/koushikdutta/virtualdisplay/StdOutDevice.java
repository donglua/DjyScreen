package com.koushikdutta.virtualdisplay;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import com.koushikdutta.async.BufferedDataSink;
import com.koushikdutta.async.ByteBufferList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;
import xyz.djy0.djyscreen.ClientConfig;
import xyz.djy0.djyscreen.util.L;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 下午12:25
 */

public class StdOutDevice extends EncoderDevice {
  public static final String TAG = StdOutDevice.class.getSimpleName();

  int bitrate;
  ByteBuffer codecPacket;
  OutputBufferCallback outputBufferCallback;
  MediaFormat outputFormat;
  BufferedDataSink sink;

  public StdOutDevice(final int n, final int n2, final BufferedDataSink sink) {
    super("stdout", n, n2);
    this.bitrate = 500000;
    this.sink = sink;
  }

  @Override
  public int getBitrate(final int n) {
    return this.bitrate;
  }

  public ByteBuffer getCodecPacket() {
    return this.codecPacket.duplicate();
  }

  public MediaFormat getOutputFormat() {
    return this.outputFormat;
  }

  @Override
  protected EncoderRunnable onSurfaceCreated(final MediaCodec mediaCodec) {
    return new Writer(mediaCodec);
  }

  @TargetApi(19)
  public void requestSyncFrame() {
    final Bundle parameters = new Bundle();
    parameters.putInt("request-sync", 0);
    this.venc.setParameters(parameters);
  }

  @TargetApi(19)
  public void setBitrate(final int bitrate) {
    L.d(this.LOGTAG, "Bitrate: " + bitrate);
    if (this.venc != null) {
      this.bitrate = bitrate;
      final Bundle parameters = new Bundle();
      parameters.putInt("video-bitrate", bitrate);
      this.venc.setParameters(parameters);
    }
  }

  public void setOutputBufferCallack(final OutputBufferCallback outputBufferCallback) {
    this.outputBufferCallback = outputBufferCallback;
  }

  class Writer extends EncoderRunnable {
    public Writer(final MediaCodec mediaCodec) {
      super(mediaCodec);
    }

    @Override
    protected void encode() throws Exception {
      L.d(StdOutDevice.this.LOGTAG, "Writer started.");
      ByteBuffer[] outputBuffers = null;
      int i = 0;
      int n = 0;
      while (i == 0) {
        final MediaCodec.BufferInfo mediaCodec$BufferInfo = new MediaCodec.BufferInfo();
        final int dequeueOutputBuffer = this.venc.dequeueOutputBuffer(mediaCodec$BufferInfo, -1L);
        if (dequeueOutputBuffer >= 0) {
          if (n == 0) {
            n = 1;
            L.d(StdOutDevice.this.LOGTAG, "Got first buffer");
          }
          if (outputBuffers == null) {
            outputBuffers = this.venc.getOutputBuffers();
          }
          final ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
          if ((0x2 & mediaCodec$BufferInfo.flags) != 0x0) {
            byteBuffer.position(mediaCodec$BufferInfo.offset);
            byteBuffer.limit(mediaCodec$BufferInfo.offset + mediaCodec$BufferInfo.size);
            (StdOutDevice.this.codecPacket = ByteBuffer.allocate(mediaCodec$BufferInfo.size)).put(byteBuffer);
            StdOutDevice.this.codecPacket.flip();
          }
          final ByteBuffer order =
              ByteBufferList.obtain(12 + mediaCodec$BufferInfo.size).order(ByteOrder.LITTLE_ENDIAN);
          order.putInt(-4 + (12 + mediaCodec$BufferInfo.size));
          order.putInt((int) TimeUnit.MICROSECONDS.toMillis(mediaCodec$BufferInfo.presentationTimeUs));
          int n2;
          if ((0x1 & mediaCodec$BufferInfo.flags) != 0x0) {
            n2 = 1;
          } else {
            n2 = 0;
          }
          order.putInt(n2);
          byteBuffer.position(mediaCodec$BufferInfo.offset);
          byteBuffer.limit(mediaCodec$BufferInfo.offset + mediaCodec$BufferInfo.size);
          order.put(byteBuffer);
          order.flip();
          byteBuffer.clear();
          StdOutDevice.this.sink.write(new ByteBufferList(new ByteBuffer[] { order }));
          if (StdOutDevice.this.outputBufferCallback != null) {
            StdOutDevice.this.outputBufferCallback.onOutputBuffer(byteBuffer, mediaCodec$BufferInfo);
          }
          this.venc.releaseOutputBuffer(dequeueOutputBuffer, false);
          if ((0x4 & mediaCodec$BufferInfo.flags) != 0x0) {
            i = 1;
          } else {
            i = 0;
          }
        } else if (dequeueOutputBuffer == -3) {
          L.d(StdOutDevice.this.LOGTAG, "MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED");
          outputBuffers = null;
        } else {
          if (dequeueOutputBuffer != -2) {
            continue;
          }
          L.d(StdOutDevice.this.LOGTAG, "MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
          StdOutDevice.this.outputFormat = this.venc.getOutputFormat();
          L.d(StdOutDevice.this.LOGTAG, "output width: " + StdOutDevice.this.outputFormat.getInteger("width"));
          L.d(StdOutDevice.this.LOGTAG, "output height: " + StdOutDevice.this.outputFormat.getInteger("height"));
        }
      }
      StdOutDevice.this.sink.end();
      L.d(StdOutDevice.this.LOGTAG, "Writer done");
    }
  }

  public static StdOutDevice current;

  public static StdOutDevice genStdOutDevice(BufferedDataSink sink) {
    if (current != null) current.stop();
    current = null;
    Point point = SurfaceControlVirtualDisplayFactory.getEncodeSize();
    current = new StdOutDevice(point.x, point.y, sink);
    if (ClientConfig.resolution != 0.0) {
      current.setUseEncodingConstraints(false);
    }
    if (Build.VERSION.SDK_INT < 19) {
      current.useSurface(false);
    }

    if (current.supportsSurface()) {
      L.d(TAG, "use virtual display");
      final SurfaceControlVirtualDisplayFactory surfaceControlVirtualDisplayFactory =
          new SurfaceControlVirtualDisplayFactory();
      current.registerVirtualDisplay(null, surfaceControlVirtualDisplayFactory, 0);
    } else {
      L.d(TAG, "use legacy path");
      current.createDisplaySurface();
    }

    return current;
  }
}
