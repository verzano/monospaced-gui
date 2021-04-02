package dev.verzano.monospaced.gui.lifecycle;

public interface LifecycleListener {
    void onStartupStart();
    void onStartupComplete();
    void onShutdownStart();
    void onShutdownComplete();
}
