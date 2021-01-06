package dev.verzano.monospaced.gui.widget;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.ansi.sgr.Background;
import dev.verzano.monospaced.core.ansi.sgr.Foreground;
import dev.verzano.monospaced.core.ansi.sgr.SgrFormat;
import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.core.metric.Spacing;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.container.Container;
import dev.verzano.monospaced.gui.container.ContainerOptions;
import dev.verzano.monospaced.gui.task.Task;
import dev.verzano.monospaced.gui.util.PrintUtils;

import java.util.*;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

// TODO make this thread safe
// padding is the space between the content and the border/label
// TODO add label
// TODO add border
// TODO add margin (space between border/label and edge of widget)
// TODO a child widget should also mark its container as focused...
public abstract class Widget {
    public static final Widget NULL_WIDGET = new Widget() {
        @Override
        public int getNeededContentHeight() {
            return 0;
        }

        @Override
        public int getNeededContentWidth() {
            return 0;
        }

        @Override
        public void printContent() {
        }
    };
    private final Map<String, Set<Task>> keyActionsMap = new HashMap<>();
    private String label = "";
    private boolean showLabel = false;
    private Position labelPosition = Position.LEFT;
    private Orientation labelOrientation = Orientation.HORIZONTAL;
    private SgrFormat labelFormat = new SgrFormat(Background.NONE, Foreground.NONE);
    private Spacing padding = new Spacing();
    private Container<? extends ContainerOptions> container = Container.NULL_CONTAINER;
    private SgrFormat focusedFormat = new SgrFormat(Background.NONE, Foreground.NONE, Attribute.NONE);
    private SgrFormat unfocusedFormat = new SgrFormat(Background.NONE, Foreground.NONE, Attribute.NONE);
    private String emptyRow;
    private String emptyContentRow;

    public abstract int getNeededContentHeight();

    public abstract int getNeededContentWidth();

    public abstract void printContent();

    public Widget() {
    }

    public Container<? extends ContainerOptions> getContainer() {
        return container;
    }

    public void setContainer(Container<? extends ContainerOptions> container) {
        this.container = container;
    }

    public SgrFormat getFocusedFormat() {
        return focusedFormat;
    }

    public void setFocusedFormat(SgrFormat focusedFormat) {
        this.focusedFormat = focusedFormat;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SgrFormat getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(SgrFormat labelFormat) {
        this.labelFormat = labelFormat;
    }

    public Orientation getLabelOrientation() {
        return labelOrientation;
    }

    public void setLabelOrientation(Orientation labelOrientation) {
        this.labelOrientation = labelOrientation;
    }

    public Position getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(Position labelPosition) {
        this.labelPosition = labelPosition;
    }

    public Spacing getPadding() {
        return padding;
    }

    public void setPadding(Spacing padding) {
        this.padding = padding;
    }

    public SgrFormat getUnfocusedFormat() {
        return unfocusedFormat;
    }

    public void setUnfocusedFormat(SgrFormat unfocusedFormat) {
        this.unfocusedFormat = unfocusedFormat;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public void addKeyAction(String key, Task action) {
        var tasks = keyActionsMap.getOrDefault(key, new HashSet<>());
        tasks.add(action);
        keyActionsMap.put(key, tasks);
    }

    public void clearKeyActions(String key) {
        keyActionsMap.remove(key);
    }

    public void fireKeyActions(String key) {
        keyActionsMap.getOrDefault(key, Collections.emptySet()).forEach(Task::fire);
    }

    public String getSgrFormatPrefix() {
        return isFocused() ? focusedFormat.getFormatString() : unfocusedFormat.getFormatString();
    }

    public int getContentHeight() {
        var contentHeight = getHeight() - padding.getTop() - padding.getBottom();
        if (showLabel) {
            switch (labelPosition) {
                case TOP, TOP_LEFT, TOP_RIGHT, BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> contentHeight -= getLabelHeight();
            }
        }
        return Math.max(contentHeight, 0);
    }

    public int getContentWidth() {
        var contentWidth = getWidth() - padding.getLeft() - padding.getRight();
        if (showLabel) {
            switch (labelPosition) {
                case LEFT, RIGHT -> contentWidth -= getLabelWidth();
            }
        }
        return Math.max(contentWidth, 0);
    }

    public int getContentX() {
        var contentX = getX() + padding.getLeft();
        if (showLabel) {
            if (labelPosition == Position.LEFT) {
                contentX += getLabelWidth();
            }
        }
        return contentX;
    }

    public int getContentY() {
        var contentY = getY() + padding.getTop();
        if (showLabel) {
            switch (labelPosition) {
                case TOP, TOP_LEFT, TOP_RIGHT -> contentY += getLabelHeight();
            }
        }
        return contentY;
    }

    public String getEmptyContentRow() {
        return getSgrFormatPrefix() + emptyContentRow + normalSgrFormat();
    }

    private String getEmptyRow() {
        return getSgrFormatPrefix() + emptyRow + normalSgrFormat();
    }

    public int getHeight() {
        return container.getWidgetHeight(this);
    }

    private int getLabelHeight() {
        return switch (labelOrientation) {
            case HORIZONTAL -> 1;
            case VERTICAL -> label.length();
        };
    }

    private int getLabelWidth() {
        return switch (labelOrientation) {
            case HORIZONTAL -> label.length();
            case VERTICAL -> 1;
        };
    }

    public int getNeededHeight() {
        int neededHeight = getNeededContentHeight();
        if (showLabel) {
            switch (labelPosition) {
                case TOP, TOP_LEFT, TOP_RIGHT, BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> neededHeight += getLabelHeight();
                case LEFT, RIGHT -> neededHeight = Math.max(getLabelHeight(), neededHeight);
            }
        }
        return neededHeight;
    }

    public int getNeededWidth() {
        int neededWidth = getNeededContentWidth();
        if (showLabel) {
            switch (labelPosition) {
                case TOP, BOTTOM -> neededWidth = Math.max(getLabelWidth(), neededWidth);
                case LEFT, TOP_LEFT, BOTTOM_LEFT, RIGHT, TOP_RIGHT, BOTTOM_RIGHT -> neededWidth += getLabelWidth();
            }
        }
        return neededWidth;
    }

    public int getWidth() {
        return container.getWidgetWidth(this);
    }

    public int getX() {
        return container.getWidgetX(this);
    }

    public int getY() {
        return container.getWidgetY(this);
    }

    public boolean isFocused() {
        return MonospacedGui.getFocusedWidget() == this;
    }

    public final void print() {
        for (var row = 0; row < getHeight(); row++) {
            MonospacedGui.move(getX(), getY() + row);
            MonospacedGui.print(getEmptyRow());
        }

        printGuts();
    }

    // TODO this method is a hack for the flaoter, need to figure out why it won't print the correct background color
    public final void printGuts() {
        if (showLabel) {
            switch (labelOrientation) {
                case VERTICAL:
//          printLabelVertical();
                    break;
                case HORIZONTAL:
                    printLabelHorizontal();
                    break;
                default:
                    break;
            }
        }

        printContent();
    }

    private void printLabelHorizontal() {
        switch (labelPosition) {
            case TOP, TOP_LEFT, TOP_RIGHT -> MonospacedGui.move(getX(), getY());
            case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> MonospacedGui.move(getX(), getY() + getHeight());
            case LEFT, RIGHT -> MonospacedGui.move(getX(), getY() / 2);
        }

        MonospacedGui.print(PrintUtils.getRowForText(label, labelPosition, labelFormat.getFormatString(), getWidth()));
    }

    public void reprint() {
        MonospacedGui.schedulePrintTask(this::print);
    }

    public void setFocused() {
        MonospacedGui.setFocusedWidget(this);
    }

    public void size() {
        emptyRow = new String(new char[getWidth()]).replace('\0', ' ');
        emptyContentRow = new String(new char[getContentWidth()]).replace('\0', ' ');
    }
}
