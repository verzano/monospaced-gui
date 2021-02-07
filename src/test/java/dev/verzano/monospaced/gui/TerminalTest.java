package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.terminal.MockTerminal;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TerminalExtension.class)
public class TerminalTest {
    protected MockTerminal terminal = MockTerminal.getInstance();
}
