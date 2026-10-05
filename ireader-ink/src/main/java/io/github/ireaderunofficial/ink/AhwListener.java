package io.github.ireaderunofficial.ink;

/**
 * Firmware auto-handwrite callbacks, delivered on the main looper (probe-verified on the Musnap
 * Ocean). The firmware has already drawn the ink when these arrive. Copy values; never retain.
 */
public interface AhwListener {
    /** MotionEvent-style actions (0 down, 2 move, 1 up, 3 cancel); surface-local px; uptime ms; pressure 0..1; tilt radians. */
    void onPoint(int action, float x, float y, long eventTime, float pressure, float tilt);

    /** End of a stroke. Also fires spuriously (sometimes much later, with no points). */
    void onTrack();
}
