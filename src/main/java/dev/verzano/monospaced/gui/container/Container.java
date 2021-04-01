package dev.verzano.monospaced.gui.container;

import dev.verzano.monospaced.core.metric.Point;
import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.widget.Widget;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class Container<T extends ContainerOptions> extends Widget {
    public static final Container<ContainerOptions> NULL_CONTAINER = new Container<>() {
        @Override
        protected void addWidgetInternal(Widget widget, @NotNull ContainerOptions options) {
        }

        @Override
        public int calculateWidgetHeight(Widget widget) {
            return 0;
        }

        @Override
        public int calculateWidgetWidth(Widget widget) {
            return 0;
        }

        @Override
        public int calculateWidgetX(Widget widget) {
            return 0;
        }

        @Override
        public int calculateWidgetY(Widget widget) {
            return 0;
        }

        @Override
        public Collection<Widget> getContainedWidgets() {
            return Collections.emptyList();
        }

        @Override
        protected void removeWidgetInternal(Widget widget) {
        }

        @Override
        protected void removeWidgetsInternal() {
        }

        @Override
        public int getNeededContentHeight() {
            return 0;
        }

        @Override
        public int getNeededContentWidth() {
            return 0;
        }
    };
    private static final Size NO_SIZE = new Size(0, 0);
    private static final Point NO_LOCATION = new Point(0, 0);
    private final Map<Widget, Size> widgetSizes = new HashMap<>();
    private final Map<Widget, Point> widgetLocations = new HashMap<>();

    protected abstract void addWidgetInternal(Widget widget, @NotNull T options);

    public abstract int calculateWidgetHeight(Widget widget);

    public abstract int calculateWidgetWidth(Widget widget);

    public abstract int calculateWidgetX(Widget widget);

    public abstract int calculateWidgetY(Widget widget);

    public abstract Collection<Widget> getContainedWidgets();

    protected abstract void removeWidgetInternal(Widget widget);

    protected abstract void removeWidgetsInternal();

    public Container() {
    }

    public void addWidget(Widget widget, @NotNull T options) {
        widgetSizes.put(widget, new Size(0, 0));
        widgetLocations.put(widget, new Point(0, 0));
        widget.setContainer(this);

        addWidgetInternal(widget, options);
        arrange();
    }

    public void arrange() {
        getContainedWidgets().forEach(widget -> {
            setWidgetWidth(widget);
            setWidgetHeight(widget);
            setWidgetX(widget);
            setWidgetY(widget);

            if (widget instanceof Container) {
                ((Container) widget).arrange();
            }
        });

        size();
    }

    public int getWidgetHeight(Widget widget) {
        return widgetSizes.getOrDefault(widget, NO_SIZE).getHeight();
    }

    public int getWidgetWidth(Widget widget) {
        return widgetSizes.getOrDefault(widget, NO_SIZE).getWidth();
    }

    public int getWidgetX(Widget widget) {
        return widgetLocations.getOrDefault(widget, NO_LOCATION).getX();
    }

    public int getWidgetY(Widget widget) {
        return widgetLocations.getOrDefault(widget, NO_LOCATION).getY();
    }

    public void removeWidget(Widget widget) {
        widgetSizes.remove(widget);
        widgetLocations.remove(widget);
        widget.setContainer(NULL_CONTAINER);

        removeWidgetInternal(widget);
        arrange();
    }

    public void removeWidgets() {
        widgetSizes.clear();
        widgetLocations.clear();
        getContainedWidgets().forEach(w -> w.setContainer(NULL_CONTAINER));

        removeWidgetsInternal();
        arrange();
    }

    public void setWidgetHeight(Widget widget) {
        widgetSizes.get(widget).setHeight(calculateWidgetHeight(widget));
    }

    public void setWidgetWidth(Widget widget) {
        widgetSizes.get(widget).setWidth(calculateWidgetWidth(widget));
    }

    public void setWidgetX(Widget widget) {
        widgetLocations.get(widget).setX(calculateWidgetX(widget));
    }

    public void setWidgetY(Widget widget) {
        widgetLocations.get(widget).setY(calculateWidgetY(widget));
    }

    @Override
    public void printContent() {
        getContainedWidgets().forEach(Widget::printGuts);
    }

    @Override
    public void setFocused() {
        var widgets = getContainedWidgets();
        if (widgets.size() > 0) {
            widgets.iterator().next().setFocused();
        } else {
            super.setFocused();
        }
    }

    @Override
    public void size() {
        super.size();
        getContainedWidgets().forEach(Widget::size);
    }
}
