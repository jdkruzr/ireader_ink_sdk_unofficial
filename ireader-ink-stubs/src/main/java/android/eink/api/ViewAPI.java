package android.eink.api;

import android.graphics.Rect;
import android.view.View;

/** Stub (subset). Per-View EPDC update modes; invalidate(view, dirty, mode) swaps the mode for one call. */
public class ViewAPI {
    public static int getUpdateMode(View view) { throw new RuntimeException("Stub!"); }
    public static void invalidate(View view, Rect dirty, int updateMode) { throw new RuntimeException("Stub!"); }
}
