package dev.verzano.monospaced.gui.widget.text;

import static dev.verzano.monospaced.core.constant.Orientation.HORIZONTAL;
import static dev.verzano.monospaced.core.constant.Orientation.VERTICAL;
import static dev.verzano.monospaced.core.constant.Position.BOTTOM;
import static dev.verzano.monospaced.core.constant.Position.BOTTOM_LEFT;
import static dev.verzano.monospaced.core.constant.Position.BOTTOM_RIGHT;
import static dev.verzano.monospaced.core.constant.Position.CENTER;
import static dev.verzano.monospaced.core.constant.Position.LEFT;
import static dev.verzano.monospaced.core.constant.Position.RIGHT;
import static dev.verzano.monospaced.core.constant.Position.TOP;
import static dev.verzano.monospaced.core.constant.Position.TOP_LEFT;
import static dev.verzano.monospaced.core.constant.Position.TOP_RIGHT;
import static dev.verzano.monospaced.gui.terminal.TixelGridMaker.makeNoStyleTixelGrid;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.TerminalTest;
import dev.verzano.monospaced.gui.terminal.Tixel;
import java.time.Duration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TextWidgetTerminalTest extends TerminalTest {
    static Object[][] print() {
        return new Object[][]{
                {7, 3, "Hello", HORIZONTAL, CENTER, makeNoStyleTixelGrid(
                        """
                                      \s
                                 Hello\s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP_LEFT, makeNoStyleTixelGrid(
                        """
                                Hello \s
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP, makeNoStyleTixelGrid(
                        """
                                 Hello\s
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP_RIGHT, makeNoStyleTixelGrid(
                        """
                                  Hello
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, RIGHT, makeNoStyleTixelGrid(
                        """
                                      \s
                                  Hello
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM_RIGHT, makeNoStyleTixelGrid(
                        """
                                      \s
                                      \s
                                  Hello
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM, makeNoStyleTixelGrid(
                        """
                                      \s
                                      \s
                                 Hello\s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM_LEFT, makeNoStyleTixelGrid(
                        """
                                      \s
                                      \s
                                Hello \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, LEFT, makeNoStyleTixelGrid(
                        """
                                      \s
                                Hello \s
                                      \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, CENTER, makeNoStyleTixelGrid(
                        """
                                  \s                                
                                 H\s
                                 e\s
                                 l\s
                                 l\s
                                 o\s
                                  \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, TOP_LEFT, makeNoStyleTixelGrid(
                        """
                                H \s
                                e \s
                                l \s
                                l \s
                                o \s
                                  \s
                                  \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, TOP, makeNoStyleTixelGrid(
                        """
                                 H\s
                                 e\s
                                 l\s
                                 l\s
                                 o\s
                                  \s
                                  \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, TOP_RIGHT, makeNoStyleTixelGrid(
                        """
                                  H
                                  e
                                  l
                                  l
                                  o
                                  \s
                                  \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, RIGHT, makeNoStyleTixelGrid(
                        """
                                  \s
                                  H
                                  e
                                  l
                                  l
                                  o
                                  \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, BOTTOM_RIGHT, makeNoStyleTixelGrid(
                        """
                                  \s
                                  \s
                                  H
                                  e
                                  l
                                  l
                                  o
                                """
                )},
                {3, 7, "Hello", VERTICAL, BOTTOM, makeNoStyleTixelGrid(
                        """
                                  \s
                                  \s
                                 H\s
                                 e\s
                                 l\s
                                 l\s
                                 o\s
                                """
                )},
                {3, 7, "Hello", VERTICAL, BOTTOM_LEFT, makeNoStyleTixelGrid(
                        """
                                  \s
                                  \s
                                H \s
                                e \s
                                l \s
                                l \s
                                o \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, LEFT, makeNoStyleTixelGrid(
                        """
                                  \s
                                H \s
                                e \s
                                l \s
                                l \s
                                o \s
                                  \s
                                """
                )},
        };
    }

    @ParameterizedTest
    @MethodSource("print")
    void print(int width, int height, String text, Orientation orientation, Position position, Tixel[][] tixels) {
        setTerminalSize(width, height);

        var tw = new TextWidget(text, orientation, position);
        MonospacedGui.setBaseWidget(tw);
        tw.print();

        assertTimeoutPreemptively(Duration.ofMillis(200), () -> {
            while (getGrid().notSameAs(tixels)) {
                Thread.sleep(5);
            }
        }, "Final grid is:\n" + getGrid().asString());
    }
}
