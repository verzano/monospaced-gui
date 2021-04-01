package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.terminal.MockTerminal;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TerminalExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
    }

    @Override
    public void beforeEach(ExtensionContext context) throws InterruptedException {
        MonospacedGui.startup(MockTerminal.getInstance());

        while(!MonospacedGui.startupComplete()) {
            Thread.sleep(5);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        MonospacedGui.shutdown();
    }

    @Override
    public void afterAll(ExtensionContext context) {
    }
}
