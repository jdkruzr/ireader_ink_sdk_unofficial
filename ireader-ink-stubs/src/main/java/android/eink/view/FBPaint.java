package android.eink.view;

import android.graphics.PorterDuff;

/** Stub. Paint for drawing through an IFBHandSurface onto the layer and/or firmware ink plane. */
public class FBPaint {
    public static final int DRAW_TARGET_SURFACE = 1;
    public static final int DRAW_TARGET_FB = 2;
    public static final int DRAW_TARGET_BOTH = 3;
    public FBPaint() { throw new RuntimeException("Stub!"); }
    public void setDrawTarget(int target) { throw new RuntimeException("Stub!"); }
    public void setPostMode(int mode) { throw new RuntimeException("Stub!"); }
    public void setColor(int color) { throw new RuntimeException("Stub!"); }
    public void setXfermode(PorterDuff.Mode mode) { throw new RuntimeException("Stub!"); }
}
