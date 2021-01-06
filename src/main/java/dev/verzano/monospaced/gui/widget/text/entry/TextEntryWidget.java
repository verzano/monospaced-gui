package dev.verzano.monospaced.gui.widget.text.entry;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.ansi.sgr.Background;
import dev.verzano.monospaced.core.ansi.sgr.Foreground;
import dev.verzano.monospaced.core.ansi.sgr.SgrFormat;
import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.widget.text.TextWidget;

import static dev.verzano.monospaced.core.ansi.sgr.Attribute.BLINK_ON;
import static dev.verzano.monospaced.core.ansi.sgr.Attribute.UNDERLINE_ON;
import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

public class TextEntryWidget extends TextWidget {
    private SgrFormat caretFormat = new SgrFormat(Background.NONE, Foreground.NONE, UNDERLINE_ON, BLINK_ON);

    public TextEntryWidget() {
        super("", Orientation.HORIZONTAL, Position.LEFT);
        // All printable ASCII chars
        Keys.printableAscii().forEach(k -> addKeyAction(k, () -> {
            setText(getText() + k);
            reprint();
        }));

        addKeyAction(Keys.DELETE, () -> {
            setText(getText().substring(0, Math.max(0, getText().length() - 1)));
            reprint();
        });

        getFocusedFormat().setAttributes(Attribute.NORMAL);
        getUnfocusedFormat().setAttributes(Attribute.NORMAL);
    }

    public SgrFormat getCaretFormat() {
        return caretFormat;
    }

    public void setCaretFormat(SgrFormat caretFormat) {
        this.caretFormat = caretFormat;
    }

    private String getCaret() {
        return isFocused() ? caretFormat.getFormatString() + " " + normalSgrFormat() : " ";
    }

    @Override
    public int getNeededContentHeight() {
        return 1;
    }

    @Override
    public int getNeededContentWidth() {
        return 1;
    }

    @Override
    public void printContent() {
        var text = getText();
        var width = getContentWidth();

        if (text.length() < width - 1) {
            text += getCaret() + new String(new char[width - text.length() - 1]).replace('\0', ' ');
        } else {
            text = text.substring(text.length() - width + 1) + getCaret();
        }

        var middleRow = getContentHeight() / 2;
        for (var i = 0; i < getContentHeight(); i++) {
            MonospacedGui.move(getContentX(), getContentY() + i);
            if (i == middleRow) {
                MonospacedGui.print(text);
            } else {
                MonospacedGui.print(getEmptyContentRow());
            }
        }
    }
}
