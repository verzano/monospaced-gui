package dev.verzano.monospaced.gui;

import static dev.verzano.monospaced.gui.terminal.MockTerminal.getInstance;
import static dev.verzano.monospaced.gui.terminal.TixelUtils.notSame;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import dev.verzano.monospaced.gui.terminal.MockTerminal;
import dev.verzano.monospaced.gui.terminal.Tixel;
import dev.verzano.monospaced.gui.terminal.TixelUtils;
import java.time.Duration;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TerminalExtension.class)
public class TerminalTest {

    protected static void setTerminalSize(int width, int height) {
        getInstance().setSize(width, height);
    }

    // TODO something a bit more complex is needed for pressing arrow keys
    protected static void simulateKeyPresses(String keys) {
        for (var key : keys.toCharArray()) {
            getInstance().getReadBuffer().add((int)key);
        }
    }

    private static String failureMessage(Tixel[][] expected) {
        return "Final grid is:\n"
                + TixelUtils.asString(getActualTixels())
                + "\nBut should be:\n"
                + TixelUtils.asString(expected);
    }

    protected static void assertTixelEquality(Tixel[][] expected) {
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            while (notSame(getActualTixels(), expected)) {
                Thread.sleep(5);
            }
        }, () -> failureMessage(expected));
    }

    private static Tixel[][] getActualTixels() {
        return MockTerminal.getInstance().getGrid().getTixels();
    }
}
