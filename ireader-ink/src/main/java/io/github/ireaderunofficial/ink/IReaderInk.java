package io.github.ireaderunofficial.ink;

import android.content.Context;
import android.eink.EPDCDevice;
import android.eink.view.HWSurfaceView;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Unofficial access to the iReader/Zhangyue firmware ink API ({@code android.eink.*}) that ships in
 * the boot classpath of iReader-platform e-ink devices (e.g. the Musnap Ocean). Nothing here is
 * packaged vendor code: the device provides the classes, this library only calls them. Every vendor
 * call is caught and logged.
 */
public final class IReaderInk {
    private static final String TAG = "IReaderInk";

    /** {@code EinkMode} value for a display-wide full GC16 refresh. */
    public static final int MODE_FULL_GC16 = 34;
    /** {@code EinkMode} value for a partial GC16 refresh. */
    public static final int MODE_PARTIAL_GC16 = 2;

    private IReaderInk() {}

    /** The firmware ink classes exist, and (where readable) the ROM declares an e-ink panel. */
    public static boolean isAvailable() {
        try {
            Class.forName("android.eink.view.FBHandSurface");
            Class.forName("android.eink.view.HWSurfaceView");
        } catch (Throwable t) {
            return false;
        }
        // Unreadable → trust the vendor classes, which no other platform ships.
        String panel = systemProperty("ro.eink.screen.type");
        return panel == null || !panel.isEmpty();
    }

    /**
     * True when the ROM's pen button is set to Eraser ({@code persist.button.erase.allow}). The
     * firmware then draws no ink and reports no points while the button is held, so the host must
     * erase from MotionEvents. Read per contact; settings changes apply without restarting.
     */
    public static boolean buttonErasesWithoutFirmwareInk() {
        return "true".equals(systemProperty("persist.button.erase.allow"));
    }

    /**
     * A SurfaceView whose holder binds a firmware handwriting surface on its first post after each
     * {@code surfaceChanged}. Callers must have hidden-API exemptions in place first: the vendor
     * {@code HWSurfaceView} reflects SurfaceView internals in its constructor.
     */
    public static SurfaceView createSurface(Context context) {
        return new HWSurfaceView(context);
    }

    /** Display-wide, non-blocking panel refresh with an {@code EinkMode} value. */
    public static void refresh(int mode) {
        try {
            EPDCDevice.forceRefresh(mode);
        } catch (Throwable t) {
            Log.w(TAG, "forceRefresh failed", t);
        }
    }

    /** Null when unreadable (JVM, or a ROM that blocks the hidden accessor). */
    public static String systemProperty(String name) {
        try {
            Object value = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, name);
            return value instanceof String ? (String) value : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
