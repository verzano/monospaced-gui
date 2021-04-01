package dev.verzano.monospaced.gui.container.enclosure;

import static dev.verzano.monospaced.core.constant.CardinalDirection.CENTER;
import static dev.verzano.monospaced.core.constant.CardinalDirection.EAST;
import static dev.verzano.monospaced.core.constant.CardinalDirection.NORTH;
import static dev.verzano.monospaced.core.constant.CardinalDirection.SOUTH;
import static dev.verzano.monospaced.core.constant.CardinalDirection.WEST;

import dev.verzano.monospaced.core.constant.CardinalDirection;
import dev.verzano.monospaced.gui.container.Container;
import dev.verzano.monospaced.gui.widget.Widget;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class Enclosure extends Container<EnclosureOptions> {
    // TODO a bidi map would be noice
    private final Map<Widget, EnclosureOptions> optionsMap = new HashMap<>();
    private final Map<CardinalDirection, Widget> widgetMap = new HashMap<>();

    public Enclosure() {
        Arrays.stream(CardinalDirection.values()).forEach(cd -> widgetMap.put(cd, NULL_WIDGET));
    }

    @Override
    protected void addWidgetInternal(Widget widget, @NotNull EnclosureOptions options) {
        Widget oldWidget = widgetMap.get(options.getPosition());
        if (oldWidget != NULL_WIDGET) {
            removeWidget(oldWidget);
        }
        optionsMap.put(widget, options);
        widgetMap.put(options.getPosition(), widget);
    }

    @Override
    public int calculateWidgetHeight(Widget widget) {
        return switch (optionsMap.get(widget).getPosition()) {
            case NORTH, SOUTH -> widget.getNeededHeight(); // TODO maybe make this fill if there aren't others...
            case EAST, WEST, CENTER -> getHeight() - getWidgetHeight(widgetMap.get(NORTH)) - getWidgetHeight(widgetMap.get(SOUTH));
        };
    }

    @Override
    public int calculateWidgetWidth(Widget widget) {
        return switch (optionsMap.get(widget).getPosition()) {
            case NORTH, SOUTH -> getWidth();
            case EAST, WEST -> widget.getNeededContentWidth(); // TODO maybe make this fill if there aren't others...
            case CENTER -> getWidth() - getWidgetWidth(widgetMap.get(EAST)) - getWidgetWidth(widgetMap.get(WEST));
        };
    }

    @Override
    public int calculateWidgetX(Widget widget) {
        return switch (optionsMap.get(widget).getPosition()) {
            case NORTH, SOUTH, WEST -> getX();
            case EAST -> getX() + getWidgetWidth(widgetMap.get(CENTER)) + getWidgetWidth(widgetMap.get(CENTER));
            case CENTER -> getX() + getWidgetWidth(widgetMap.get(WEST));
        };
    }

    @Override
    public int calculateWidgetY(Widget widget) {
        return switch (optionsMap.get(widget).getPosition()) {
            case NORTH -> getY();
            case EAST, WEST, CENTER -> getY() + getWidgetHeight(widgetMap.get(NORTH));
            case SOUTH -> getY() + getWidgetHeight(widgetMap.get(CENTER));
        };
    }

    @Override
    public Collection<Widget> getContainedWidgets() {
        return widgetMap.values().stream().filter(w -> w != NULL_WIDGET).collect(Collectors.toList());
    }

    @Override
    protected void removeWidgetInternal(Widget widget) {
        EnclosureOptions options = optionsMap.remove(widget);
        widgetMap.remove(options.getPosition());
    }

    @Override
    protected void removeWidgetsInternal() {
        optionsMap.clear();
        Arrays.stream(CardinalDirection.values()).forEach(cd -> widgetMap.put(cd, NULL_WIDGET));
    }

    @Override
    public void arrange() {
        Widget north = widgetMap.get(NORTH);
        Widget south = widgetMap.get(SOUTH);
        Widget east = widgetMap.get(EAST);
        Widget west = widgetMap.get(WEST);
        Widget center = widgetMap.get(CENTER);

        // Size
        if (north != NULL_WIDGET) {
            setWidgetHeight(north);
            setWidgetWidth(north);
        }

        if (south != NULL_WIDGET) {
            setWidgetHeight(south);
            setWidgetWidth(south);
        }

        if (east != NULL_WIDGET) {
            setWidgetHeight(east);
            setWidgetWidth(east);
        }

        if (west != NULL_WIDGET) {
            setWidgetHeight(west);
            setWidgetWidth(west);
        }

        if (center != NULL_WIDGET) {
            setWidgetHeight(center);
            setWidgetWidth(center);
        }

        // Location
        if (north != NULL_WIDGET) {
            setWidgetX(north);
            setWidgetY(north);
        }

        if (south != NULL_WIDGET) {
            setWidgetX(south);
            setWidgetY(south);
        }

        if (east != NULL_WIDGET) {
            setWidgetX(east);
            setWidgetY(east);
        }

        if (west != NULL_WIDGET) {
            setWidgetX(west);
            setWidgetY(west);
        }

        if (center != NULL_WIDGET) {
            setWidgetX(center);
            setWidgetY(center);
        }

        if (north instanceof Container) {
            ((Container) north).arrange();
        }

        if (south instanceof Container) {
            ((Container) south).arrange();
        }

        if (east instanceof Container) {
            ((Container) east).arrange();
        }

        if (west instanceof Container) {
            ((Container) west).arrange();
        }

        if (center instanceof Container) {
            ((Container) center).arrange();
        }

        size();
    }

    @Override
    public int getNeededContentHeight() {
        var northHeight = widgetMap.getOrDefault(NORTH, NULL_WIDGET).getNeededHeight();
        var southHeight = widgetMap.getOrDefault(SOUTH, NULL_WIDGET).getNeededHeight();

        var leftHeight = northHeight + widgetMap.getOrDefault(WEST, NULL_WIDGET).getNeededHeight() + southHeight;
        var middleHeight = northHeight + widgetMap.getOrDefault(CENTER, NULL_WIDGET).getNeededHeight() + southHeight;
        var rightHeight = northHeight + widgetMap.getOrDefault(EAST, NULL_WIDGET).getNeededHeight() + southHeight;
        return Math.max(leftHeight, Math.max(middleHeight, rightHeight));
    }

    @Override
    public int getNeededContentWidth() {
        var northWidth = widgetMap.getOrDefault(NORTH, NULL_WIDGET).getNeededWidth();
        var southWidth = widgetMap.getOrDefault(SOUTH, NULL_WIDGET).getNeededWidth();
        var middleWidth = widgetMap.getOrDefault(WEST, NULL_WIDGET).getNeededWidth()
                + widgetMap.getOrDefault(CENTER, NULL_WIDGET).getNeededWidth()
                + widgetMap.getOrDefault(EAST, NULL_WIDGET).getNeededWidth();
        return Math.max(northWidth, Math.max(southWidth, middleWidth));
    }
}
