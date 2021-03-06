package dev.verzano.monospaced.gui.widget.scrollable.text;

import dev.verzano.monospaced.core.constant.Direction;
import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.widget.scrollable.ScrollableWidget;

import java.util.LinkedList;
import java.util.List;

public class TextAreaWidget extends ScrollableWidget {
    private String text;
    private List<String> lines;
    private volatile int topLine;

    public TextAreaWidget() {
        this("");
    }

    public TextAreaWidget(String text) {
        setText(text);

        addKeyAction(Keys.UP_ARROW, () -> {
            scroll(Direction.UP, 1);
            reprint();
        });
        addKeyAction(Keys.DOWN_ARROW, () -> {
            scroll(Direction.DOWN, 1);
            reprint();
        });
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        calculateLines();
        setTopLine(0);
    }

    private void setTopLine(int topLine) {
        this.topLine = topLine;
        setViewTop(topLine);
    }

    private void calculateLines() {
        lines = new LinkedList<>();

        var width = getWidth() - 1;

        for (var chunk : text.split("\n")) {
            if (chunk.isEmpty()) {
                continue;
            }

            var begin = 0;
            var end = width;

            while (end < chunk.length()) {
                var lastSpace = chunk.substring(begin, end).lastIndexOf(' ') + begin + 1;
                var line = chunk.substring(begin, lastSpace);
                lines.add(line + new String(new char[width - line.length()]).replace('\0', ' '));

                begin = lastSpace;
                end = begin + width;
            }

            var line = chunk.substring(begin);
            lines.add(line + new String(new char[width - line.length()]).replace('\0', ' '));
            lines.add(new String(new char[width]).replace('\0', ' '));
        }

        setInternalHeight(lines.size());
    }

    @Override
    public int getNeededContentHeight() {
        var width = getWidth() - 1;
        return lines.stream().mapToInt(row -> (int) Math.ceil(row.length() / width)).sum();
    }

    @Override
    public int getNeededContentWidth() {
        return lines.stream().mapToInt(String::length).max().orElse(0) + 1;
    }

    @Override
    public void size() {
        super.size();
        calculateLines();
    }

    @Override
    public void scroll(Direction direction, int distance) {
        switch (direction) {
            case UP:
                setTopLine(Math.max(0, topLine - distance));
                break;
            case DOWN:
                if (topLine + getHeight() < lines.size()) {
                    setTopLine(topLine + distance);
                }
                break;
        }
    }

    @Override
    public void printContent() {
        super.printContent();

        var width = getContentWidth() - 1;

        for (var row = 0; row <= getContentHeight(); row++) {
            MonospacedGui.move(getContentX(), getContentY() + row);
            var line = row + topLine;
            if (line < lines.size()) {
                MonospacedGui.print(lines.get(line));
            } else {
                MonospacedGui.printn(" ", width);
            }
        }
    }
}
