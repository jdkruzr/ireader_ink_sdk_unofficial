package android.eink.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

/**
 * Stub. A SurfaceView whose holder binds an FBHandSurface to its layer on the first
 * unlockCanvasAndPost after each surfaceChanged. Construct only after HiddenApiBypass.
 */
public class HWSurfaceView extends SurfaceView {
    public HWSurfaceView(Context context) { super(context); throw new RuntimeException("Stub!"); }
    public IFBHandSurface getFBSurface() { throw new RuntimeException("Stub!"); }
    /** Canvas over the already-posted front buffer; unlocking does not queue a new frame. */
    public Canvas lockActiveCanvas() { throw new RuntimeException("Stub!"); }
    public void unlockActiveCanvas() { throw new RuntimeException("Stub!"); }
}
