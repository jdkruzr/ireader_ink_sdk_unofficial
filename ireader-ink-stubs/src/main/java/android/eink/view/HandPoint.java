package android.eink.view;

import android.graphics.PointF;

/** Stub. Reused by the firmware across callbacks: copy fields out, never retain it. */
public class HandPoint extends PointF {
    public int action;
    public long eventtime;
    public float pressure;
    public float tilt;
    public HandPoint() { throw new RuntimeException("Stub!"); }
}
