package com.koushikdutta.virtualdisplay;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import org.xml.sax.Attributes;
import xyz.djy0.djyscreen.util.L;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 下午12:43
 */

public abstract class EncoderDevice {
  protected final String LOGTAG;
  int colorFormat;
  Point encSize;
  protected int height;
  protected Thread lastRecorderThread;
  public String name;
  boolean useEncodingConstraints;
  boolean useSurface;
  protected VirtualDisplayFactory vdf;
  protected MediaCodec venc;
  protected VirtualDisplay virtualDisplay;
  protected int width;

  public EncoderDevice(final String name, final int width, final int height) {
    this.LOGTAG = this.getClass().getSimpleName();
    this.useSurface = true;
    this.useEncodingConstraints = true;
    this.width = width;
    this.height = height;
    this.name = name;
    L.d(this.LOGTAG, "Requested Width: " + width + " Requested Height: " + height);
  }

  public static int getSupportedDimension(final int n) {
    int n2 = 144;
    if (n > n2) {
      if (n <= 176) {
        n2 = 176;
      } else if (n <= 240) {
        n2 = 240;
      } else if (n <= 288) {
        n2 = 288;
      } else if (n <= 320) {
        n2 = 320;
      } else if (n <= 352) {
        n2 = 352;
      } else if (n <= 480) {
        n2 = 480;
      } else if (n <= 576) {
        n2 = 576;
      } else if (n <= 720) {
        n2 = 720;
      } else if (n <= 1024) {
        n2 = 1024;
      } else if (n <= 1280) {
        n2 = 1280;
      } else {
        n2 = 1920;
      }
    }
    return n2;
  }

  private static boolean isRecognizedFormat(final int n) {
    boolean b = false;
    switch (n) {
      default: {
        b = false;
        break;
      }
      case 19:
      case 20:
      case 21:
      case 39:
      case 2130706688: {
        b = true;
        break;
      }
    }
    return b;
  }

  private int selectColorFormat(final MediaCodecInfo mediaCodecInfo, final String s) throws Exception {
    final MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(s);
    L.d(this.LOGTAG, "Available color formats: " + capabilitiesForType.colorFormats.length);
    for (int i = 0; i < capabilitiesForType.colorFormats.length; ++i) {
      final int n = capabilitiesForType.colorFormats[i];
      if (isRecognizedFormat(n)) {
        L.d(this.LOGTAG, "Using: " + n);
        return n;
      }
      L.d(this.LOGTAG, "Not using: " + n);
    }
    throw new Exception("Unable to find suitable color format");
  }

  public Surface createDisplaySurface() {
    int i = Build.VERSION.SDK_INT;
    n = 18;
    int i1 = i;
    if (i >= n) {
      signalEnd();
    }
    i = 0;
    Object localObject1 = null;
    i1 = 0;
    localObject3 = null;
    localObject4 = this;
    this.venc = null;
    localObject5 = null;
    try
    {
      int i6 = MediaCodecList.getCodecCount();
      int i7 = 0;
      MediaCodecInfo localMediaCodecInfo;
      for (;;)
      {
        i1 = i7;
        if (i7 >= i6) {
          break label466;
        }
        localMediaCodecInfo = MediaCodecList.getCodecInfoAt(i7);
        j = localMediaCodecInfo.isEncoder();
        if (j != 0) {
          break;
        }
        i7 += 1;
      }
      Object localObject6 = localMediaCodecInfo.getSupportedTypes();
      localObject3 = localObject6;
      i1 = localObject6.length;
      int i8 = i1;
      int j = 0;
      localObject1 = null;
      n = 0;
      localObject7 = null;
      int k;
      for (;;)
      {
        i1 = n;
        if (n >= i8) {
          break;
        }
        Object localObject8 = localObject6[n];
        localObject1 = "video/avc";
        localObject3 = localObject8;
        localObject4 = localObject1;
        j = ((String)localObject8).equalsIgnoreCase((String)localObject1);
        if (j != 0)
        {
          if (localObject5 == null) {
            localObject5 = localMediaCodecInfo;
          }
          localObject3 = this;
          localObject3 = this.LOGTAG;
          localObject1 = localObject3;
          Object localObject9 = localMediaCodecInfo.getName();
          localObject4 = localObject9;
          Log.i((String)localObject3, (String)localObject9);
          localObject1 = "video/avc";
          localObject3 = localObject1;
          MediaCodecInfo.CodecCapabilities localCodecCapabilities = localMediaCodecInfo.getCapabilitiesForType((String)localObject1);
          localObject3 = localCodecCapabilities.colorFormats;
          localObject9 = localObject3;
          i1 = localObject3.length;
          int i9 = i1;
          j = 0;
          localObject1 = null;
          Object localObject10;
          Object localObject11;
          String str1;
          for (;;)
          {
            i1 = j;
            if (j >= i9) {
              break;
            }
            int i10 = localObject9[j];
            localObject3 = this;
            localObject3 = this.LOGTAG;
            localObject10 = localObject3;
            localObject11 = new java/lang/StringBuilder;
            ((StringBuilder)localObject11).<init>();
            str1 = "colorFormat: ";
            localObject11 = ((StringBuilder)localObject11).append(str1);
            localObject3 = localObject11;
            localObject11 = ((StringBuilder)localObject11).append(i10);
            localObject11 = ((StringBuilder)localObject11).toString();
            Log.i((String)localObject10, (String)localObject11);
            j += 1;
          }
          localObject3 = localCodecCapabilities.profileLevels;
          localObject9 = localObject3;
          i1 = localObject3.length;
          i9 = i1;
          k = 0;
          localObject1 = null;
          for (;;)
          {
            i1 = k;
            if (k >= i9) {
              break;
            }
            Object localObject12 = localObject9[k];
            localObject3 = this;
            localObject3 = this.LOGTAG;
            localObject10 = localObject3;
            localObject11 = new java/lang/StringBuilder;
            ((StringBuilder)localObject11).<init>();
            str1 = "profile/level: ";
            localObject11 = ((StringBuilder)localObject11).append(str1);
            localObject3 = localObject12;
            i1 = ((MediaCodecInfo.CodecProfileLevel)localObject12).profile;
            localObject11 = ((StringBuilder)localObject11).append(i1);
            str1 = "/";
            localObject11 = ((StringBuilder)localObject11).append(str1);
            i1 = ((MediaCodecInfo.CodecProfileLevel)localObject12).level;
            localObject11 = ((StringBuilder)localObject11).append(i1);
            localObject11 = ((StringBuilder)localObject11).toString();
            Log.i((String)localObject10, (String)localObject11);
            k += 1;
          }
        }
        k = n + 1;
        n = k;
      }
      try
      {
        label466:
        String str2;
        h localh;
        c localc;
        localCamcorderProfile = CamcorderProfile.get(k);
      }
      catch (Exception localException5)
      {
        for (;;)
        {
          ArrayList localArrayList;
          localObject3 = this;
          localObject3 = this.LOGTAG;
          localObject2 = localObject3;
          localObject7 = "Error getting camcorder profiles";
          localObject4 = localObject7;
          Log.e((String)localObject3, (String)localObject7, localException5);
        }
      }
    }
    catch (Exception localException1)
    {
      localObject2 = "/system/etc/media_profiles.xml";
      try
      {
        str2 = StreamUtility.readFile((String)localObject2);
        localh = new a/h;
        localObject2 = "MediaSettings";
        localObject3 = localh;
        localObject4 = localObject2;
        localh.<init>((String)localObject2);
        localObject2 = "VideoEncoderCap";
        localObject4 = localObject2;
        localc = localh.b((String)localObject2);
        localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        localObject2 = new com/koushikdutta/virtualdisplay/EncoderDevice$1;
        localObject3 = localObject2;
        localObject4 = this;
        ((EncoderDevice.1)localObject2).<init>(this, localArrayList);
        localc.a((d)localObject2);
        localObject2 = new java/io/StringReader;
        localObject3 = localObject2;
        localObject4 = str2;
        ((StringReader)localObject2).<init>(str2);
        localObject7 = localh.b();
        g.a((Reader)localObject2, (ContentHandler)localObject7);
        k = localArrayList.size();
        n = 1;
        i1 = k;
        if (k == n) {
          break label1767;
        }
        localObject2 = new java/lang/Exception;
        localObject7 = "derp";
        ((Exception)localObject2).<init>((String)localObject7);
        throw ((Throwable)localObject2);
      }
      catch (Exception localException2)
      {
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        localObject7 = "Error getting media profiles";
        localObject4 = localObject7;
        Log.e((String)localObject3, (String)localObject7, localException2);
        localCamcorderProfile = null;
        k = 6;
      }
    }
    if (localCamcorderProfile == null) {
      k = 5;
    }
    try
    {
      localCamcorderProfile = CamcorderProfile.get(k);
    }
    catch (Exception localException6)
    {
      for (;;)
      {
        int i15;
        int i16;
        double d1;
        int i17;
        double d2;
        double d3;
        MediaFormat localMediaFormat;
        label1444:
        label1767:
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        localObject7 = "Error getting camcorder profiles";
        localObject4 = localObject7;
        Log.e((String)localObject3, (String)localObject7, localException6);
        continue;
        localObject3 = localCamcorderProfile;
        int i11 = localCamcorderProfile.videoFrameWidth;
        int i12 = localCamcorderProfile.videoFrameHeight;
        int i13 = localCamcorderProfile.videoBitRate;
        i5 = localCamcorderProfile.videoFrameRate;
        int i14 = i5;
        continue;
        i5 = this.height;
        m = i5;
        if (i5 > i15)
        {
          i5 = i15;
          d1 = i15;
          i5 = this.height;
          i17 = i5;
          d2 = i5;
          d3 = d1 / d2;
          i5 = i15;
          localObject4 = this;
          this.height = i15;
          i5 = this.width;
          m = i5;
          d2 = i5;
          d1 = d2;
          d1 = d2 * d3;
          d2 = d1;
          i5 = (int)d1;
          m = i5;
          this.width = i5;
        }
        localObject3 = this;
        i5 = this.width;
        m = i5;
        if (i5 > i16)
        {
          i5 = i16;
          d1 = i16;
          i5 = this.width;
          i17 = i5;
          d2 = i5;
          d3 = d1 / d2;
          i5 = i16;
          localObject4 = this;
          this.width = i16;
          i5 = this.height;
          m = i5;
          d2 = i5;
          d1 = d2;
          d1 = d2 * d3;
          d2 = d1;
          i5 = (int)d1;
          m = i5;
          this.height = i5;
          continue;
          localObject2 = "color-format";
          localObject7 = "video/avc";
          localObject3 = this;
          localObject4 = localObject7;
          n = selectColorFormat((MediaCodecInfo)localObject5, (String)localObject7);
          i5 = n;
          localObject4 = this;
          this.colorFormat = n;
          localObject3 = localMediaFormat;
          localObject4 = localObject2;
          localMediaFormat.setInteger((String)localObject2, n);
        }
      }
    }
    if (localCamcorderProfile == null)
    {
      i11 = 640;
      i12 = 480;
      i13 = 2000000;
      for (i14 = 30;; i14 = i5)
      {
        localObject3 = this;
        int i2 = this.useEncodingConstraints;
        k = i2;
        if (i2 != 0)
        {
          i2 = i11;
          i15 = Math.max(i11, i12);
          i16 = Math.min(i11, i12);
          k = this.width;
          n = this.height;
          i2 = k;
          if (k <= n) {
            break;
          }
          i3 = this.width;
          k = i3;
          if (i3 > i15)
          {
            i3 = i15;
            d1 = i15;
            i3 = this.width;
            i17 = i3;
            d2 = i3;
            d3 = d1 / d2;
            i3 = i15;
            localObject4 = this;
            this.width = i15;
            i3 = this.height;
            k = i3;
            d2 = i3;
            d1 = d2;
            d1 = d2 * d3;
            d2 = d1;
            i3 = (int)d1;
            k = i3;
            this.height = i3;
          }
          localObject3 = this;
          i3 = this.height;
          k = i3;
          if (i3 > i16)
          {
            i3 = i16;
            d1 = i16;
            i3 = this.height;
            i17 = i3;
            d2 = i3;
            d3 = d1 / d2;
            i3 = i16;
            localObject4 = this;
            this.height = i16;
            i3 = this.width;
            k = i3;
            d2 = i3;
            d1 = d2;
            d1 = d2 * d3;
            d2 = d1;
            i3 = (int)d1;
            k = i3;
            this.width = i3;
          }
        }
        localObject3 = this;
        int i3 = this.width;
        k = i3;
        k = i3 / 16;
        i3 = k;
        localObject4 = this;
        this.width = k;
        i3 = this.width;
        k = i3;
        k = i3 * 16;
        i3 = k;
        this.width = k;
        i3 = this.height;
        k = i3;
        k = i3 / 16;
        i3 = k;
        this.height = k;
        i3 = this.height;
        k = i3;
        k = i3 * 16;
        i3 = k;
        this.height = k;
        localObject2 = this.LOGTAG;
        localObject7 = new java/lang/StringBuilder;
        ((StringBuilder)localObject7).<init>();
        localObject7 = ((StringBuilder)localObject7).append("Width: ");
        localObject3 = this;
        i3 = this.width;
        i17 = i3;
        localObject7 = ((StringBuilder)localObject7).append(i3).append(" Height: ");
        i3 = this.height;
        i17 = i3;
        localObject7 = i3;
        Log.i((String)localObject2, (String)localObject7);
        localObject2 = new android/graphics/Point;
        n = this.width;
        i3 = this.height;
        i17 = i3;
        ((Point)localObject2).<init>(n, i3);
        localObject3 = localObject2;
        this.encSize = ((Point)localObject2);
        localObject3 = this;
        n = this.width;
        i3 = this.height;
        i17 = i3;
        localMediaFormat = MediaFormat.createVideoFormat("video/avc", n, i3);
        i13 = getBitrate(i13);
        localObject2 = this.LOGTAG;
        localObject7 = new java/lang/StringBuilder;
        ((StringBuilder)localObject7).<init>();
        localObject7 = ((StringBuilder)localObject7).append("Bitrate: ");
        localObject3 = localObject7;
        localObject7 = i13;
        Log.i((String)localObject2, (String)localObject7);
        localObject3 = localMediaFormat;
        localObject4 = "bitrate";
        localMediaFormat.setInteger((String)localObject4, i13);
        localObject4 = "frame-rate";
        localMediaFormat.setInteger((String)localObject4, i14);
        localObject3 = this;
        localObject2 = this.LOGTAG;
        localObject7 = new java/lang/StringBuilder;
        ((StringBuilder)localObject7).<init>();
        localObject6 = "Frame rate: ";
        localObject7 = ((StringBuilder)localObject7).append((String)localObject6);
        localObject3 = localObject7;
        localObject7 = i14;
        Log.i((String)localObject2, (String)localObject7);
        localObject7 = TimeUnit.MILLISECONDS;
        i17 = 1000 / i14;
        i3 = i17;
        long l1 = i17;
        long l2 = l1;
        l2 = ((TimeUnit)localObject7).toMicros(l1);
        localObject3 = localMediaFormat;
        localObject4 = "repeat-previous-frame-after";
        localMediaFormat.setLong((String)localObject4, l2);
        n = 30;
        localObject4 = "i-frame-interval";
        localMediaFormat.setInteger((String)localObject4, n);
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        localObject7 = "Creating encoder";
        Log.i((String)localObject3, (String)localObject7);
        try
        {
          boolean bool = supportsSurface();
          if (bool)
          {
            localObject3 = this;
            localObject4 = localMediaFormat;
            setSurfaceFormat(localMediaFormat);
            localObject2 = "video/avc";
          }
        }
        catch (Exception localException3)
        {
          String str3;
          Surface localSurface;
          int m;
          int i4;
          EncoderDevice.EncoderRunnable localEncoderRunnable;
          Object localObject13;
          int i5;
          localObject3 = this;
          localObject3 = this.LOGTAG;
          localObject2 = localObject3;
          localObject4 = "Exception creating venc";
          Log.e((String)localObject3, (String)localObject4, localException3);
          localObject2 = new java/lang/AssertionError;
          localObject3 = localObject2;
          ((AssertionError)localObject2).<init>(localException3);
          throw ((Throwable)localObject2);
        }
        try
        {
          localObject2 = MediaCodec.createEncoderByType((String)localObject2);
          localObject3 = localObject2;
          localObject4 = this;
          this.venc = ((MediaCodec)localObject2);
        }
        catch (Exception localException4)
        {
          localObject3 = this;
          localObject3 = this.LOGTAG;
          localObject2 = localObject3;
          localObject7 = "Unable to create codec by type, attempting explicit creation";
          localObject4 = localObject7;
          Log.e((String)localObject3, (String)localObject7, localException4);
          localObject2 = ((MediaCodecInfo)localObject5).getName();
          localObject2 = MediaCodec.createByCodecName((String)localObject2);
          localObject3 = localObject2;
          localObject4 = this;
          this.venc = ((MediaCodec)localObject2);
          break label1444;
        }
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        Log.i((String)localObject3, "Created encoder");
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        Log.i((String)localObject3, "Configuring encoder");
        localObject3 = this;
        localObject3 = this.venc;
        localObject2 = localObject3;
        i17 = 0;
        localObject6 = null;
        i8 = 1;
        localObject4 = localMediaFormat;
        str3 = null;
        ((MediaCodec)localObject3).configure(localMediaFormat, null, null, i8);
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        localObject7 = "Creating input surface";
        Log.i((String)localObject3, (String)localObject7);
        localSurface = null;
        m = Build.VERSION.SDK_INT;
        n = 18;
        i3 = m;
        if (m >= n)
        {
          localObject3 = this;
          i4 = this.useSurface;
          m = i4;
          if (i4 != 0)
          {
            localObject3 = this.venc;
            localObject2 = localObject3;
            localSurface = ((MediaCodec)localObject3).createInputSurface();
          }
        }
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        Log.i((String)localObject3, "Starting Encoder");
        localObject3 = this;
        localObject3 = this.venc;
        localObject2 = localObject3;
        ((MediaCodec)localObject3).start();
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        Log.i((String)localObject3, "Surface ready");
        localObject3 = this;
        localObject2 = this.venc;
        localObject3 = this;
        localObject4 = localObject2;
        localEncoderRunnable = onSurfaceCreated((MediaCodec)localObject2);
        localObject2 = new java/lang/Thread;
        localObject3 = localObject2;
        localObject4 = localEncoderRunnable;
        str3 = "Encoder";
        ((Thread)localObject2).<init>(localEncoderRunnable, str3);
        localObject4 = this;
        this.lastRecorderThread = ((Thread)localObject2);
        localObject3 = this;
        localObject3 = this.lastRecorderThread;
        localObject2 = localObject3;
        ((Thread)localObject3).start();
        localObject3 = this;
        localObject3 = this.LOGTAG;
        localObject2 = localObject3;
        Log.i((String)localObject3, "Encoder ready");
        return localSurface;
        m = 0;
        localObject2 = null;
        i4 = 0;
        localObject3 = null;
        localObject13 = localArrayList.get(0);
        localObject13 = (EncoderDevice.VideoEncoderCap)localObject13;
        localObject3 = localObject13;
        i5 = ((EncoderDevice.VideoEncoderCap)localObject13).maxFrameWidth;
        i11 = i5;
        i5 = ((EncoderDevice.VideoEncoderCap)localObject13).maxFrameHeight;
        i12 = i5;
        i13 = ((EncoderDevice.VideoEncoderCap)localObject13).maxBitRate;
        i5 = ((EncoderDevice.VideoEncoderCap)localObject13).maxFrameRate;
      }
    }
  }

  @TargetApi(18)
  Surface createInputSurface() {
    return this.venc.createInputSurface();
  }

  void destroyDisplaySurface(final MediaCodec mediaCodec) {
    if (mediaCodec != null) {
      while (true) {
        try {
          mediaCodec.stop();
          mediaCodec.release();
          if (this.venc == mediaCodec) {
            this.venc = null;
            if (this.virtualDisplay != null) {
              this.virtualDisplay.release();
              this.virtualDisplay = null;
            }
            if (this.vdf != null) {
              this.vdf.release();
              this.vdf = null;
            }
          }
        } catch (Exception ex) {
          continue;
        }
        break;
      }
    }
  }

  public int getBitrate(final int n) {
    return 2000000;
  }

  public int getColorFormat() {
    return this.colorFormat;
  }

  public Point getEncodingDimensions() {
    return this.encSize;
  }

  public MediaCodec getMediaCodec() {
    return this.venc;
  }

  public boolean isConnected() {
    return this.venc != null;
  }

  public void joinRecorderThread() {
    try {
      if (this.lastRecorderThread != null) {
        this.lastRecorderThread.join();
      }
    } catch (InterruptedException ex) {
    }
  }

  protected abstract EncoderRunnable onSurfaceCreated(final MediaCodec p0);

  public void registerVirtualDisplay(final Context context, final VirtualDisplayFactory vdf, final int n) {
    assert this.virtualDisplay == null;
    final Surface displaySurface = this.createDisplaySurface();
    if (displaySurface == null) {
      L.d(this.LOGTAG, "Unable to create surface");
    } else {
      L.d(this.LOGTAG, "Created surface");
      this.vdf = vdf;
      this.virtualDisplay = vdf.createVirtualDisplay(this.name, this.width, this.height, n, 3, displaySurface, null);
    }
  }

  @TargetApi(18)
  void setSurfaceFormat(final MediaFormat mediaFormat) {
    mediaFormat.setInteger("color-format", this.colorFormat = 2130708361);
  }

  public void setUseEncodingConstraints(final boolean useEncodingConstraints) {
    this.useEncodingConstraints = useEncodingConstraints;
  }

  @TargetApi(18)
  void signalEnd() {
    if (this.venc == null) {
      return;
    }
    try {
      this.venc.signalEndOfInputStream();
    } catch (Exception ex) {
    }
  }

  public void stop() {
    if (Build.VERSION.SDK_INT >= 18) {
      this.signalEnd();
    }
    this.venc = null;
    if (this.virtualDisplay != null) {
      this.virtualDisplay.release();
      this.virtualDisplay = null;
    }
    if (this.vdf != null) {
      this.vdf.release();
      this.vdf = null;
    }
  }

  public boolean supportsSurface() {
    return Build.VERSION.SDK_INT >= 19 && this.useSurface;
  }

  public void useSurface(final boolean useSurface) {
    this.useSurface = useSurface;
  }

  protected abstract class EncoderRunnable implements Runnable {
    MediaCodec venc;

    public EncoderRunnable(final MediaCodec venc) {
      this.venc = venc;
    }

    protected void cleanup() {
      EncoderDevice.this.destroyDisplaySurface(this.venc);
      this.venc = null;
    }

    protected abstract void encode() throws Exception;

    @Override
    public final void run() {
      while (true) {
        try {
          this.encode();
          this.cleanup();
          L.d(EncoderDevice.this.LOGTAG, "=======ENCODING COMPELTE=======");
        } catch (Exception ex) {
          L.d(EncoderDevice.this.LOGTAG, "Encoder error" + (Throwable) ex);
          continue;
        }
        break;
      }
    }
  }

  private static class VideoEncoderCap {
    int maxBitRate;
    int maxFrameHeight;
    int maxFrameRate;
    int maxFrameWidth;

    public VideoEncoderCap(final Attributes attributes) {
      this.maxFrameWidth = Integer.valueOf(attributes.getValue("maxFrameWidth"));
      this.maxFrameHeight = Integer.valueOf(attributes.getValue("maxFrameHeight"));
      this.maxBitRate = Integer.valueOf(attributes.getValue("maxBitRate"));
      this.maxFrameRate = Integer.valueOf(attributes.getValue("maxFrameRate"));
    }
  }
}
