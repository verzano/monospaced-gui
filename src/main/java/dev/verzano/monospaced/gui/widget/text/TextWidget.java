package dev.verzano.monospaced.gui.widget.text;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.util.PrintUtils;
import dev.verzano.monospaced.gui.widget.Widget;

public class TextWidget extends Widget {
    private String text;
    private Orientation orientation;
    private Position textPosition;

    public TextWidget() {
        this("", Orientation.HORIZONTAL, Position.LEFT);
    }

    public TextWidget(String text) {
        this(text, Orientation.HORIZONTAL, Position.LEFT);
    }

    public TextWidget(String text, Orientation orientation, Position textPosition) {
        this.text = text;
        this.textPosition = textPosition;
        this.orientation = orientation;

        getFocusedFormat().setAttributes(Attribute.INVERSE_ON);
        getUnfocusedFormat().setAttributes(Attribute.INVERSE_ON);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Position getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(Position textPosition) {
        this.textPosition = textPosition;
    }

    private void printHorizontal() {
        switch (textPosition) {
            case TOP_LEFT, TOP, TOP_RIGHT -> {
                MonospacedGui.move(getContentX(), getContentY());
                MonospacedGui.print(PrintUtils.getRowForText(text, textPosition, getSgrFormatPrefix(), getWidth()));
                for (var i = 1; i < getContentHeight(); i++) {
                    MonospacedGui.move(getContentX(), getContentY() + i);
                    MonospacedGui.print(getEmptyContentRow());
                }
            }
            case LEFT, CENTER, RIGHT -> {
                var middleRow = getContentHeight() / 2;
                for (var i = 0; i < getContentHeight(); i++) {
                    MonospacedGui.move(getContentX(), getContentY() + i);
                    if (i == middleRow) {
                        MonospacedGui.print(PrintUtils.getRowForText(text, textPosition, getSgrFormatPrefix(), getWidth()));
                    } else {
                        MonospacedGui.print(getEmptyContentRow());
                    }
                }
            }
            case BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT -> {
                MonospacedGui.move(getContentX(), getContentY());
                for (var i = 1; i < getContentHeight(); i++) {
                    MonospacedGui.print(getEmptyContentRow());
                    MonospacedGui.move(getContentX(), getContentY() + i);
                }
                MonospacedGui.print(PrintUtils.getRowForText(text, textPosition, getSgrFormatPrefix(), getWidth()));
            }
        }
    }

    @Override
    public int getNeededContentHeight() {
        return switch (orientation) {
            case VERTICAL -> text.length();
            case HORIZONTAL -> 1;
        };
    }

    @Override
    public int getNeededContentWidth() {
        return switch (orientation) {
            case VERTICAL -> 1;
            case HORIZONTAL -> text.length();
        };
    }

    @Override
    public void printContent() {
        switch (orientation) {
            // TODO make vertical printContent correctly
            case VERTICAL -> {
                MonospacedGui.move(getContentX(), getContentY());
                String toPrint = text;
                for (int row = 0; row < getContentHeight(); row++) {
                    MonospacedGui.move(getContentX(), getContentY() + row);
                    if (row < toPrint.length()) {
                        MonospacedGui.print(getSgrFormatPrefix() + toPrint.charAt(row) + normalSgrFormat());
                    } else {
                        MonospacedGui.print(getSgrFormatPrefix() + " " + normalSgrFormat());
                    }
                }
            }
            case HORIZONTAL -> printHorizontal();
        }
    }
}
