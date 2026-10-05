# iReader Ink SDK (Unofficial)

Small Android library for the firmware "auto handwrite" (AHW) ink API that iReader/Zhangyue-platform
e-ink devices ship in their boot classpath (`android.eink.*`), e.g. the Musnap Ocean. The firmware
draws live ink straight to the panel and reports each stroke's points afterwards.

Nothing vendor-owned is bundled. The device provides the classes; `ireader-ink-stubs` only supplies
compile-time signatures and must be a `compileOnly` dependency.

## Modules

- `ireader-ink`: the API.
  - `IReaderInk`: availability check, system properties and the pen-button erase property. It
    references no vendor types, so it's safe on any device and on the JVM.
  - `AhwSession`: `createSurface` and `refresh`, plus binding a surface's firmware handwriting, with
    a transition-only gate, pen configuration and an `AhwListener` for points and stroke ends.
- `ireader-ink-stubs`: `android.eink.*` signatures copied from decompiled firmware. Never package it.

## Using It

```kotlin
// settings.gradle.kts of the consuming build
includeBuild("../ireader_ink_sdk_unofficial") {
    dependencySubstitution {
        substitute(module("io.github.ireaderunofficial:ireader-ink")).using(project(":ireader-ink"))
        substitute(module("io.github.ireaderunofficial:ireader-ink-stubs")).using(project(":ireader-ink-stubs"))
    }
}
// module build.gradle.kts
implementation("io.github.ireaderunofficial:ireader-ink")
```

```kotlin
if (IReaderInk.isAvailable()) {
    // HWSurfaceView reflects SurfaceView internals: install hidden-API exemptions first
    // (e.g. org.lsposed.hiddenapibypass).
    val surface = AhwSession.createSurface(context)
    val session = AhwSession()
    session.connect(surface, listener)   // gate starts closed
    // After surfaceChanged: post one frame (binds the firmware surface), then:
    session.setPen(4, 5f, Color.BLACK)
    session.setLive(true)
}
```

## Firmware Behaviour

Measured on the Musnap Ocean (Android 14, firmware 3.1.10). See the consuming app's research notes
for the full list.

- The firmware handwriting surface binds on the first `unlockCanvasAndPost` after each
  `surfaceChanged`.
- AHW is on when a surface is constructed. `AhwSession.connect` closes it.
- Callbacks arrive on the main looper:
  - actions are MotionEvent style (0, 2…2, 1; 3 cancels);
  - `eventtime` is on the `uptimeMillis` clock;
  - pressure is 0..1 and tilt is in radians.
- A stroke's end is reported twice (UP or CANCEL, then `onTrack`), and `onTrack` also fires
  spuriously. Deduplicate.
- With the system pen button set to Eraser, the firmware withholds ink and points while the button
  is held. Check `IReaderInk.buttonErasesWithoutFirmwareInk()`.
- The ROM defers any window frame posted during handwriting and flushes it as a full-window refresh
  about 300 ms after pen-up. A pen-down during that refresh stalls live ink. Post nothing while the
  pen is active, and hide the status bar.
