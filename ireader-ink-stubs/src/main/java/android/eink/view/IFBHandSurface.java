package android.eink.view;

import android.graphics.Path;
import android.graphics.PorterDuff;

/** Stub (subset). Handle to the firmware auto-handwrite surface bound to a SurfaceFlinger layer. */
public interface IFBHandSurface {
    void disableAutoHandWrite(Object who);
    void enableAutoHandWrite(Object who);
    boolean isAutoHandWrite();
    Object[] dumpAhwDisableList();
    long getAhwLastPointEventTime();
    void setAhwPenType(int type);
    void setAhwPenWidth(float width);
    void setAhwPenColor(int color);
    void setAhwPenPostMode(int mode);
    void setAhwEnableLineMode(boolean enable);
    void setAhwToolTypes(int[] toolTypes);
    void setAhwTouchListenerV2(IAhwTouchListenerV2 listener);
    void setClip(Path path);
    void save();
    void restore();
    void drawColor(int color, PorterDuff.Mode mode, FBPaint paint);
    void invalidate(int mode);
    void invalidate(int left, int top, int right, int bottom, int mode);
}
