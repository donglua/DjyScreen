package com.koushikdutta.virtualdisplay;

import android.os.Handler;
import android.view.Surface;

/**
 * @author wuwen E-mail: wuwen@tigerbrokers.com
 * @version 创建时间: 2016/10/14 下午4:14
 */

public interface VirtualDisplayFactory {
  VirtualDisplay createVirtualDisplay(final String p0, final int p1, final int p2, final int p3, final int p4,
      final Surface p5, final Handler p6);

  void release();
}
