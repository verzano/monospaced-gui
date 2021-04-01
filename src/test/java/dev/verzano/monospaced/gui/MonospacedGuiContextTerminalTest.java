package dev.verzano.monospaced.gui;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MonospacedGuiContextTerminalTest extends TerminalTest {
    @Test
    void sizeMatchesTerminalSize() {
        final var height = 30;
        final var width = 80;
        setTerminalSize(width, height);

        var floor = MonospacedGui.getContext().getFloor();

        assertAll(
                () -> assertEquals(height, floor.getHeight()),
                () -> assertEquals(width, floor.getWidth())
        );
    }

    @Test
    void neededContentSizeMatchesTerminalSize() {
        final var height = 30;
        final var width = 80;
        setTerminalSize(width, height);

        var floor = MonospacedGui.getContext().getFloor();

        assertAll(
                () -> assertEquals(height, floor.getNeededContentHeight()),
                () -> assertEquals(width, floor.getNeededContentWidth())
        );
    }
}
