// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.support.v4.util.Pools;
import java.util.concurrent.ArrayBlockingQueue;
import android.view.View;
import android.util.AttributeSet;
import android.support.annotation.UiThread;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.support.annotation.LayoutRes;
import android.os.Message;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.LayoutInflater;
import android.os.Handler$Callback;
import android.os.Handler;

public final class AsyncLayoutInflater
{
    private static final String TAG = "AsyncLayoutInflater";
    Handler mHandler;
    private Handler$Callback mHandlerCallback;
    InflateThread mInflateThread;
    LayoutInflater mInflater;
    
    public AsyncLayoutInflater(@NonNull final Context context) {
        this.mHandlerCallback = (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                final InflateRequest inflateRequest = (InflateRequest)message.obj;
                if (inflateRequest.view == null) {
                    inflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
                }
                inflateRequest.callback.onInflateFinished(inflateRequest.view, inflateRequest.resid, inflateRequest.parent);
                AsyncLayoutInflater.this.mInflateThread.releaseRequest(inflateRequest);
                return true;
            }
        };
        this.mInflater = new BasicInflater(context);
        this.mHandler = new Handler(this.mHandlerCallback);
        this.mInflateThread = InflateThread.getInstance();
    }
    
    @UiThread
    public void inflate(@LayoutRes final int resid, @Nullable final ViewGroup parent, @NonNull final OnInflateFinishedListener callback) {
        if (callback == null) {
            throw new NullPointerException("callback argument may not be null!");
        }
        final InflateRequest obtainRequest = this.mInflateThread.obtainRequest();
        obtainRequest.inflater = this;
        obtainRequest.resid = resid;
        obtainRequest.parent = parent;
        obtainRequest.callback = callback;
        this.mInflateThread.enqueue(obtainRequest);
    }
    
    private static class BasicInflater extends LayoutInflater
    {
        private static final String[] sClassPrefixList;
        
        static {
            sClassPrefixList = new String[] { "android.widget.", "android.webkit.", "android.app." };
        }
        
        BasicInflater(final Context context) {
            super(context);
        }
        
        public LayoutInflater cloneInContext(final Context context) {
            return new BasicInflater(context);
        }
        
        protected View onCreateView(final String s, final AttributeSet set) throws ClassNotFoundException {
            final String[] sClassPrefixList = BasicInflater.sClassPrefixList;
            final int length = sClassPrefixList.length;
            int n = 0;
            while (true) {
                if (n >= length) {
                    return super.onCreateView(s, set);
                }
                final String s2 = sClassPrefixList[n];
                try {
                    final View view = this.createView(s, s2, set);
                    if (view != null) {
                        return view;
                    }
                }
                catch (ClassNotFoundException ex) {}
                ++n;
            }
        }
    }
    
    private static class InflateRequest
    {
        OnInflateFinishedListener callback;
        AsyncLayoutInflater inflater;
        ViewGroup parent;
        int resid;
        View view;
    }
    
    private static class InflateThread extends Thread
    {
        private static final InflateThread sInstance;
        private ArrayBlockingQueue<InflateRequest> mQueue;
        private Pools.SynchronizedPool<InflateRequest> mRequestPool;
        
        static {
            (sInstance = new InflateThread()).start();
        }
        
        private InflateThread() {
            this.mQueue = new ArrayBlockingQueue<InflateRequest>(10);
            this.mRequestPool = (Pools.SynchronizedPool<InflateRequest>)new Pools.SynchronizedPool(10);
        }
        
        public static InflateThread getInstance() {
            return InflateThread.sInstance;
        }
        
        public void enqueue(final InflateRequest inflateRequest) {
            try {
                this.mQueue.put(inflateRequest);
            }
            catch (InterruptedException ex) {
                throw new RuntimeException("Failed to enqueue async inflate request", ex);
            }
        }
        
        public InflateRequest obtainRequest() {
            InflateRequest inflateRequest = this.mRequestPool.acquire();
            if (inflateRequest == null) {
                inflateRequest = new InflateRequest();
            }
            return inflateRequest;
        }
        
        public void releaseRequest(final InflateRequest inflateRequest) {
            inflateRequest.callback = null;
            inflateRequest.inflater = null;
            inflateRequest.parent = null;
            inflateRequest.resid = 0;
            inflateRequest.view = null;
            this.mRequestPool.release(inflateRequest);
        }
        
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: getfield        android/support/v4/view/AsyncLayoutInflater$InflateThread.mQueue:Ljava/util/concurrent/ArrayBlockingQueue;
            //     4: invokevirtual   java/util/concurrent/ArrayBlockingQueue.take:()Ljava/lang/Object;
            //     7: checkcast       Landroid/support/v4/view/AsyncLayoutInflater$InflateRequest;
            //    10: astore_3       
            //    11: aload_3        
            //    12: aload_3        
            //    13: getfield        android/support/v4/view/AsyncLayoutInflater$InflateRequest.inflater:Landroid/support/v4/view/AsyncLayoutInflater;
            //    16: getfield        android/support/v4/view/AsyncLayoutInflater.mInflater:Landroid/view/LayoutInflater;
            //    19: aload_3        
            //    20: getfield        android/support/v4/view/AsyncLayoutInflater$InflateRequest.resid:I
            //    23: aload_3        
            //    24: getfield        android/support/v4/view/AsyncLayoutInflater$InflateRequest.parent:Landroid/view/ViewGroup;
            //    27: iconst_0       
            //    28: invokevirtual   android/view/LayoutInflater.inflate:(ILandroid/view/ViewGroup;Z)Landroid/view/View;
            //    31: putfield        android/support/v4/view/AsyncLayoutInflater$InflateRequest.view:Landroid/view/View;
            //    34: aload_3        
            //    35: getfield        android/support/v4/view/AsyncLayoutInflater$InflateRequest.inflater:Landroid/support/v4/view/AsyncLayoutInflater;
            //    38: getfield        android/support/v4/view/AsyncLayoutInflater.mHandler:Landroid/os/Handler;
            //    41: iconst_0       
            //    42: aload_3        
            //    43: invokestatic    android/os/Message.obtain:(Landroid/os/Handler;ILjava/lang/Object;)Landroid/os/Message;
            //    46: invokevirtual   android/os/Message.sendToTarget:()V
            //    49: goto            0
            //    52: astore_1       
            //    53: ldc             "AsyncLayoutInflater"
            //    55: aload_1        
            //    56: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/Throwable;)I
            //    59: pop            
            //    60: goto            0
            //    63: astore          4
            //    65: ldc             "AsyncLayoutInflater"
            //    67: ldc             "Failed to inflate resource in the background! Retrying on the UI thread"
            //    69: aload           4
            //    71: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    74: pop            
            //    75: goto            34
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                            
            //  -----  -----  -----  -----  --------------------------------
            //  0      11     52     63     Ljava/lang/InterruptedException;
            //  11     34     63     78     Ljava/lang/RuntimeException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0034:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:138)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
    
    public interface OnInflateFinishedListener
    {
        void onInflateFinished(final View p0, final int p1, final ViewGroup p2);
    }
}
