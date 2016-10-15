package com.koushikdutta.virtualdisplay;

import android.media.MediaCodec;
import java.nio.ByteBuffer;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/15 下午1:45
 */

public interface OutputBufferCallback {
  void onOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
}
