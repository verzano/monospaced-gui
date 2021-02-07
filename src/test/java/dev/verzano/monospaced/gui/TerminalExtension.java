package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.terminal.MockTerminal;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TerminalExtension implements BeforeEachCallback, BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        MonospacedGui.init(MockTerminal.getInstance());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        MockTerminal.getInstance().reset();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        MonospacedGui.shutdown();
    }
}
