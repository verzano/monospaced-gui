package dev.verzano.monospaced.gui.container.floor;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.verzano.monospaced.gui.TerminalTest;
import org.junit.jupiter.api.Test;

class FloorTest extends TerminalTest {

    @Test
    void matchesTerminalSize() {
        final var height = 30;
        terminal.setHeight(height);
        final var width = 80;
        terminal.setWidth(width);

        var floor = new Floor();
        assertAll(
                () -> assertEquals(height, floor.getHeight()),
                () -> assertEquals(width, floor.getWidth())
        );
    }
}