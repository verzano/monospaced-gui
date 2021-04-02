package dev.verzano.monospaced.gui.widget.text.entry;

import static dev.verzano.monospaced.gui.terminal.TixelUtils.toNoStyleGrid;

import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.TerminalTest;
import dev.verzano.monospaced.gui.terminal.Tixel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TextEntryWidgetTerminalTest extends TerminalTest {
    static Object[][] typing() {
        return new Object[][]{
                {7, 1, "Hello", toNoStyleGrid(
                        """
                                Hello \s
                                """
                )},
                {7, 1, "Hello\u0008\u0008\u0008\u0008\u0008", toNoStyleGrid(
                        """
                                      \s
                                """
                )},
        };
    }

    @ParameterizedTest
    @MethodSource("typing")
    void typing(int width, int height, String keys, Tixel[][] expected) {
        setTerminalSize(width, height);

        var tew = new TextEntryWidget();
        MonospacedGui.setBaseWidget(tew);
        tew.setFocused();

        simulateKeyPresses(keys);

        assertTixelEquality(expected);
    }
}