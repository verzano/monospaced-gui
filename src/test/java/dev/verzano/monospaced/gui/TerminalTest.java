package dev.verzano.monospaced.gui;

import dev.verzano.monospaced.gui.terminal.MockTerminal;
import dev.verzano.monospaced.gui.terminal.TerminalGrid;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TerminalExtension.class)
public class TerminalTest {

    protected void setTerminalSize(int width, int height) {
        MockTerminal.getInstance().setSize(width, height);
    }

    protected TerminalGrid getGrid() {
        return MockTerminal.getInstance().getGrid();
    }
}
