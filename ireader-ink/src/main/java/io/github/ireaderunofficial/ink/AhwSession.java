package io.github.ireaderunofficial.ink;

import android.eink.view.FBHandSurface;
import android.eink.view.HWSurfaceView;
import android.eink.view.IAhwTouchListenerV2;
import android.eink.view.IFBHandSurface;
import android.util.Log;
import android.view.SurfaceView;

/**
 * One binding to a surface's firmware auto handwrite (AHW). Main thread only. Findings from the
 * Musnap Ocean that shape it:
 * <ul>
 *   <li>A new surface starts with AHW on; {@link #connect} closes it before anything can ink.</li>
 *   <li>The firmware's disable list is an ArrayList that accepts duplicates, so {@link #setLive}
 *       calls the vendor only on transitions, with one owner token.</li>
 *   <li>Never call {@link #disconnect} from inside a firmware callback (vendor
 *       ConcurrentModificationException).</li>
 * </ul>
 */
public final class AhwSession {
    private static final String TAG = "IReaderInk";

    private final Object token = new Object();
    private IFBHandSurface handle;
    private IAhwTouchListenerV2 callback;
    /** Null until connected; then the last state pushed to the vendor. */
    private Boolean live;

    /** Take {@code surface}'s firmware handle, close the gate, register {@code listener}. False if unusable. */
    public boolean connect(SurfaceView surface, final AhwListener listener) {
        disconnect();
        if (!(surface instanceof HWSurfaceView)) {
            Log.e(TAG, "input surface is " + surface.getClass().getName() + ", not HWSurfaceView; no firmware ink");
            return false;
        }
        try {
            IFBHandSurface fb = ((HWSurfaceView) surface).getFBSurface();
            fb.disableAutoHandWrite(token);
            live = false;
            fb.setAhwEnableLineMode(false);
            IAhwTouchListenerV2 v2 = new IAhwTouchListenerV2() {
                @Override public void onAhwAddPoint(IAhwTouchListenerV2.ParamAddPoint p) {
                    // Param objects are reused by the firmware: copy fields now.
                    listener.onPoint(p.point.action, p.point.x, p.point.y, p.point.eventtime, p.point.pressure, p.point.tilt);
                }
                @Override public void onAhwAddTrack(IAhwTouchListenerV2.ParamAddTrack t) { listener.onTrack(); }
                @Override public void onAhwSetPen(IAhwTouchListenerV2.ParamSetPen p) {}
            };
            ((FBHandSurface) fb).addAhwTouchListenerV2(v2);
            handle = fb;
            callback = v2;
            return true;
        } catch (Throwable t) {
            Log.w(TAG, "connect failed", t);
            return false;
        }
    }

    /** Close the gate and unregister. */
    public void disconnect() {
        IFBHandSurface fb = handle;
        if (fb == null) return;
        try {
            setLive(false);
            if (callback != null && fb instanceof FBHandSurface) ((FBHandSurface) fb).removeAhwTouchListenerV2(callback);
        } catch (Throwable t) {
            Log.w(TAG, "disconnect failed", t);
        }
        handle = null;
        callback = null;
        live = null;
    }

    /** Open or close firmware ink. Calls the vendor only on transitions. */
    public void setLive(boolean value) {
        IFBHandSurface fb = handle;
        if (fb == null || (live != null && live == value)) return;
        try {
            if (value) fb.enableAutoHandWrite(token); else fb.disableAutoHandWrite(token);
            live = value;
            Log.d(TAG, "gate " + (value ? "open" : "closed"));
        } catch (Throwable t) {
            Log.w(TAG, "setLive(" + value + ") failed", t);
        }
    }

    /** Firmware pen: vendor pen type, width in surface px (not zoom-scaled), ARGB colour. */
    public void setPen(int type, float widthPx, int color) {
        IFBHandSurface fb = handle;
        if (fb == null) return;
        try {
            fb.setAhwPenType(type);
            fb.setAhwPenWidth(widthPx);
            fb.setAhwPenColor(color);
        } catch (Throwable t) {
            Log.w(TAG, "setPen failed", t);
        }
    }
}
