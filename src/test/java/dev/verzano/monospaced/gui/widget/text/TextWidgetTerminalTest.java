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
import static dev.verzano.monospaced.gui.terminal.TixelUtils.toNoStyleGrid;

import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.TerminalTest;
import dev.verzano.monospaced.gui.terminal.Tixel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TextWidgetTerminalTest extends TerminalTest {
    static Object[][] print() {
        return new Object[][]{
                {7, 3, "Hello", HORIZONTAL, CENTER, toNoStyleGrid(
                        """
                                      \s
                                 Hello\s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP_LEFT, toNoStyleGrid(
                        """
                                Hello \s
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP, toNoStyleGrid(
                        """
                                 Hello\s
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, TOP_RIGHT, toNoStyleGrid(
                        """
                                  Hello
                                      \s
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, RIGHT, toNoStyleGrid(
                        """
                                      \s
                                  Hello
                                      \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM_RIGHT, toNoStyleGrid(
                        """
                                      \s
                                      \s
                                  Hello
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM, toNoStyleGrid(
                        """
                                      \s
                                      \s
                                 Hello\s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, BOTTOM_LEFT, toNoStyleGrid(
                        """
                                      \s
                                      \s
                                Hello \s
                                """
                )},
                {7, 3, "Hello", HORIZONTAL, LEFT, toNoStyleGrid(
                        """
                                      \s
                                Hello \s
                                      \s
                                """
                )},
                {3, 7, "Hello", VERTICAL, CENTER, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, TOP_LEFT, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, TOP, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, TOP_RIGHT, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, RIGHT, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, BOTTOM_RIGHT, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, BOTTOM, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, BOTTOM_LEFT, toNoStyleGrid(
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
                {3, 7, "Hello", VERTICAL, LEFT, toNoStyleGrid(
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
    void print(int width, int height, String text, Orientation orientation, Position position, Tixel[][] expected) {
        setTerminalSize(width, height);

        var tw = new TextWidget(text, orientation, position);
        MonospacedGui.setBaseWidget(tw);
        tw.print();

        assertTixelEquality(expected);
    }
}
