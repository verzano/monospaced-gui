package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.lifecycle.LifecycleListenerAdapter;
import dev.verzano.monospaced.gui.terminal.MockTerminal;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TerminalExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    private final AtomicBoolean startupComplete = new AtomicBoolean(false);
    private final AtomicBoolean shutdownComplete = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext context) {
        MonospacedGui.enableLogging(System.out);
        MonospacedGui.addLifecycleListener(new LifecycleListenerAdapter() {
            @Override
            public void onStartupComplete() {
                startupComplete.set(true);
            }

            @Override
            public void onShutdownComplete() {
                shutdownComplete.set(true);
            }
        });
    }

    @Override
    public void beforeEach(ExtensionContext context) throws InterruptedException {
        startupComplete.set(false);
        shutdownComplete.set(false);

        MonospacedGui.startup(MockTerminal.getInstance());

        while(!startupComplete.get()) {
            Thread.sleep(50);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws InterruptedException {
        MonospacedGui.shutdown();

        while(!shutdownComplete.get()) {
            Thread.sleep(50);
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
    }
}
