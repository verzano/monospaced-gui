package dev.verzano.monospaced.gui.container.floor;

import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.container.Container;
import dev.verzano.monospaced.gui.widget.Widget;

import java.util.Collection;
import java.util.Collections;

import static dev.verzano.monospaced.core.metric.Size.FILL_CONTAINER;
import static dev.verzano.monospaced.core.metric.Size.FILL_NEEDED;

public class Floor extends Container<FloorOptions> {
    private FloorOptions options = null;
    private Widget widget = NULL_WIDGET;

    public Floor() {

    }

    @Override
    public void addWidgetInternal(Widget widget, FloorOptions options) {
        if (this.widget != NULL_WIDGET) {
            removeWidget(this.widget);
        }
        this.widget = widget;
        this.options = options;
    }

    @Override
    public int calculateWidgetHeight(Widget widget) {
        return switch (options.getSize().getHeight()) {
            case FILL_CONTAINER -> MonospacedGui.getHeight();
            case FILL_NEEDED -> widget.getNeededHeight();
            default -> options.getSize().getHeight();
        };
    }

    @Override
    public int calculateWidgetWidth(Widget widget) {
        return switch (options.getSize().getWidth()) {
            case FILL_CONTAINER -> MonospacedGui.getWidth();
            case FILL_NEEDED -> widget.getNeededContentWidth();
            default -> options.getSize().getWidth();
        };
    }

    @Override
    public int calculateWidgetX(Widget widget) {
        return options.getLocation().getX();
    }

    @Override
    public int calculateWidgetY(Widget widget) {
        return options.getLocation().getY();
    }

    @Override
    public Collection<Widget> getContainedWidgets() {
        return Collections.singleton(widget);
    }

    @Override
    public void removeWidgetInternal(Widget widget) {
        this.widget = NULL_WIDGET;
        options = null;
    }

    @Override
    public void removeWidgetsInternal() {
        widget = NULL_WIDGET;
        options = null;
    }

    @Override
    public int getNeededContentHeight() {
        return MonospacedGui.getHeight();
    }

    @Override
    public int getNeededContentWidth() {
        return MonospacedGui.getWidth();
    }

    @Override
    public int getHeight() {
        return MonospacedGui.getHeight();
    }

    @Override
    public int getWidth() {
        return MonospacedGui.getWidth();
    }
}