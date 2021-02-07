package dev.verzano.monospaced.gui.widget.text;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TextWidgetTest {
    static Object[][] testData() {
        return new Object[][] {
                {"", Orientation.HORIZONTAL, Position.LEFT},
                {"", Orientation.HORIZONTAL, Position.RIGHT},
                {"", Orientation.VERTICAL, Position.LEFT},
                {"", Orientation.VERTICAL, Position.RIGHT},
                {" ", Orientation.HORIZONTAL, Position.LEFT},
                {" ", Orientation.HORIZONTAL, Position.RIGHT},
                {" ", Orientation.VERTICAL, Position.LEFT},
                {" ", Orientation.VERTICAL, Position.RIGHT},
                {"Test", Orientation.HORIZONTAL, Position.LEFT},
                {"Test", Orientation.HORIZONTAL, Position.RIGHT},
                {"Test", Orientation.VERTICAL, Position.LEFT},
                {"Test", Orientation.VERTICAL, Position.RIGHT},
        };
    }

    @Test
    void defaultValues() {
        var widget = new TextWidget();

        assertAll(
                () -> assertEquals("", widget.getText()),
                () -> assertEquals(Orientation.HORIZONTAL, widget.getOrientation()),
                () -> assertEquals(Position.LEFT, widget.getTextPosition())
        );
    }

    @Test
    void textOnlyDefaultValues() {
        final var text = "Some sample text";
        var widget = new TextWidget(text);

        assertAll(
                () -> assertEquals(text, widget.getText()),
                () -> assertEquals(Orientation.HORIZONTAL, widget.getOrientation()),
                () -> assertEquals(Position.LEFT, widget.getTextPosition())
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void neededContentSize(String text, Orientation orientation, Position position) {
        var widget = new TextWidget(text, orientation, position);
        var expectedWidth = orientation == Orientation.HORIZONTAL ? text.length() : 1;
        var expectedHeight = orientation == Orientation.HORIZONTAL ? 1 : text.length();

        assertAll(
                () -> assertEquals(orientation, widget.getOrientation()),
                () -> assertEquals(text, widget.getText()),
                () -> assertEquals(expectedWidth, widget.getNeededContentWidth()),
                () -> assertEquals(expectedHeight, widget.getNeededContentHeight())
        );
    }
}