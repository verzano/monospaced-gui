package dev.verzano.monospaced.gui.container.shelf;

import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.container.Container;
import dev.verzano.monospaced.gui.widget.Widget;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class Shelf extends Container<ShelfOptions> {
    private final List<Widget> widgetStack = new LinkedList<>();
    private final Map<Widget, ShelfOptions> optionsMap = new HashMap<>();
    private final Orientation orientation;
    private int spacing;

    public Shelf(Orientation orientation, int spacing) {
        this.orientation = orientation;
        this.spacing = spacing;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    @Override
    protected void addWidgetInternal(Widget widget, @NotNull ShelfOptions options) {
        optionsMap.put(widget, options);
        widgetStack.add(widget);
    }

    @Override
    public int calculateWidgetHeight(Widget widget) {
        var height = optionsMap.get(widget).getSize().getHeight();

        return switch (height) {
            case Size.FILL_CONTAINER -> getHeight();
            case Size.FILL_NEEDED -> widget.getNeededHeight();
            default -> height;
        };
    }

    @Override
    public int calculateWidgetWidth(Widget widget) {
        var width = optionsMap.get(widget).getSize().getWidth();

        return switch (width) {
            case Size.FILL_CONTAINER -> getWidth();
            case Size.FILL_NEEDED -> widget.getNeededWidth();
            default -> width;
        };
    }

    @Override
    public int calculateWidgetX(Widget widget) {
        var x = getX();
        if (orientation == Orientation.HORIZONTAL) {
            for (Widget w : widgetStack) {
                if (widget == w) {
                    break;
                }

                x += getWidgetWidth(w) + spacing;
            }
        }
        return x + getPadding().getLeft();
    }

    @Override
    public int calculateWidgetY(Widget widget) {
        var y = getY();
        if (orientation == Orientation.VERTICAL) {
            for (Widget w : widgetStack) {
                if (widget == w) {
                    break;
                }

                y += getWidgetHeight(w) + spacing;
            }
        }
        return y + getPadding().getTop();
    }

    @Override
    public Collection<Widget> getContainedWidgets() {
        return widgetStack;
    }

    @Override
    protected void removeWidgetInternal(Widget widget) {
        optionsMap.remove(widget);
        widgetStack.remove(widget);
    }

    @Override
    protected void removeWidgetsInternal() {
        optionsMap.clear();
        widgetStack.clear();
    }

    @Override
    public int getNeededContentHeight() {
        var height = 0;
        if (!widgetStack.isEmpty()) {
            height = switch (orientation) {
                case HORIZONTAL -> widgetStack.stream()
                        .mapToInt(Widget::getHeight)
                        .max()
                        .orElse(0);
                case VERTICAL -> (widgetStack.size() - 1) * spacing + widgetStack.stream()
                        .mapToInt(Widget::getHeight)
                        .sum();
            };
        }

        return height + getPadding().getTop() + getPadding().getBottom();
    }

    @Override
    public int getNeededContentWidth() {
        var width = 0;
        if (!widgetStack.isEmpty()) {
            width = switch (orientation) {
                case HORIZONTAL -> (widgetStack.size() - 1) * spacing + widgetStack.stream()
                        .mapToInt(Widget::getWidth)
                        .sum();
                case VERTICAL -> widgetStack.stream()
                        .mapToInt(Widget::getWidth)
                        .max()
                        .orElse(0);
            };
        }
        return width + getPadding().getLeft() + getPadding().getRight();
    }
}
