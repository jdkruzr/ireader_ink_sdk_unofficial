package android.eink.view;

/** Stub. Firmware auto-handwrite mirror: reports what the firmware already drew. */
public interface IAhwTouchListenerV2 {
    void onAhwAddPoint(ParamAddPoint p);
    void onAhwAddTrack(ParamAddTrack t);
    void onAhwSetPen(ParamSetPen p);

    class ParamSetPen {
        public static final int ACTION_USER_SET_PEN = 0;
        public static final int ACTION_ENTER_LINE_MODE = 1;
        public static final int ACTION_EXIT_LINE_MODE = 2;
        public int action;
        public int color;
        public long tag;
        public int type;
        public float width;
        public ParamSetPen() { throw new RuntimeException("Stub!"); }
    }

    class ParamAddPoint {
        public HandPoint point;
        public ParamAddPoint() { throw new RuntimeException("Stub!"); }
    }

    class ParamAddTrack {
        public float bottom;
        public float left;
        public float right;
        public float top;
        public ParamAddTrack() { throw new RuntimeException("Stub!"); }
    }
}
