package com.verzano.monospaced.gui.container.enclosure;

import com.verzano.monospaced.gui.constant.CardinalDirection;
import com.verzano.monospaced.gui.container.Container;
import com.verzano.monospaced.gui.widget.Widget;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.verzano.monospaced.gui.constant.CardinalDirection.*;

public class Enclosure extends Container<EnclosureOptions> {
  // TODO a bidi map would be noice
  private Map<Widget, EnclosureOptions> optionsMap = new HashMap<>();
  private Map<CardinalDirection, Widget> widgetMap = new HashMap<>();

  public Enclosure() {
    Arrays.stream(CardinalDirection.values()).forEach(cd -> widgetMap.put(cd, NULL_WIDGET));
  }

  @Override
  public void addWidgetInternal(Widget widget, EnclosureOptions options) {
    Widget oldWidget = widgetMap.get(options.getPosition());
    if(oldWidget != NULL_WIDGET) {
      removeWidget(oldWidget);
    }
    optionsMap.put(widget, options);
    widgetMap.put(options.getPosition(), widget);
  }

  @Override
  public int calculateWidgetHeight(Widget widget) {
    int height = 0;
    switch(optionsMap.get(widget).getPosition()) {
      case NORTH:
      case SOUTH:
        // TODO maybe make this fill if there aren't others...
        height = widget.getNeededHeight();
        break;
      case EAST:
      case WEST:
      case CENTER:
        height = getHeight() - getWidgetHeight(widgetMap.get(NORTH)) - getWidgetHeight(widgetMap.get(SOUTH));
        break;
    }
    return height;
  }

  @Override
  public int calculateWidgetWidth(Widget widget) {
    int width = 0;
    switch(optionsMap.get(widget).getPosition()) {
      case NORTH:
      case SOUTH:
        width = getWidth();
        break;
      case EAST:
      case WEST:
        // TODO maybe make this fill if there aren't others...
        width = widget.getNeededContentWidth();
        break;
      case CENTER:
        width = getWidth() - getWidgetWidth(widgetMap.get(EAST)) - getWidgetWidth(widgetMap.get(WEST));
        break;
    }
    return width;
  }

  @Override
  public int calculateWidgetX(Widget widget) {
    int x = getX();
    switch(optionsMap.get(widget).getPosition()) {
      case NORTH:
      case SOUTH:
      case WEST:
        break;
      case EAST:
        x += getWidgetWidth(widgetMap.get(CENTER)) + getWidgetWidth(widgetMap.get(CENTER));
        break;
      case CENTER:
        x += getWidgetWidth(widgetMap.get(WEST));
        break;
    }
    return x;
  }

  @Override
  public int calculateWidgetY(Widget widget) {
    int y = getY();
    switch(optionsMap.get(widget).getPosition()) {
      case NORTH:
        break;
      case EAST:
      case WEST:
      case CENTER:
        y += getWidgetHeight(widgetMap.get(NORTH));
        break;
      case SOUTH:
        y += getWidgetHeight(widgetMap.get(CENTER));
        break;
    }
    return y;
  }

  @Override
  public Collection<Widget> getContainedWidgets() {
    return widgetMap.values().stream().filter(w -> w != NULL_WIDGET).collect(Collectors.toList());
  }

  @Override
  public void removeWidgetInternal(Widget widget) {
    EnclosureOptions options = optionsMap.remove(widget);
    widgetMap.remove(options.getPosition());
  }

  @Override
  public void removeWidgetsInternal() {
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
    if(north != NULL_WIDGET) {
      setWidgetHeight(north);
      setWidgetWidth(north);
    }

    if(south != NULL_WIDGET) {
      setWidgetHeight(south);
      setWidgetWidth(south);
    }

    if(east != NULL_WIDGET) {
      setWidgetHeight(east);
      setWidgetWidth(east);
    }

    if(west != NULL_WIDGET) {
      setWidgetHeight(west);
      setWidgetWidth(west);
    }

    if(center != NULL_WIDGET) {
      setWidgetHeight(center);
      setWidgetWidth(center);
    }

    // Location
    if(north != NULL_WIDGET) {
      setWidgetX(north);
      setWidgetY(north);
    }

    if(south != NULL_WIDGET) {
      setWidgetX(south);
      setWidgetY(south);
    }

    if(east != NULL_WIDGET) {
      setWidgetX(east);
      setWidgetY(east);
    }

    if(west != NULL_WIDGET) {
      setWidgetX(west);
      setWidgetY(west);
    }

    if(center != NULL_WIDGET) {
      setWidgetX(center);
      setWidgetY(center);
    }

    if(north instanceof Container) {
      ((Container)north).arrange();
    }

    if(south instanceof Container) {
      ((Container)south).arrange();
    }

    if(east instanceof Container) {
      ((Container)east).arrange();
    }

    if(west instanceof Container) {
      ((Container)west).arrange();
    }

    if(center instanceof Container) {
      ((Container)center).arrange();
    }

    size();
  }

  @Override
  public int getNeededContentHeight() {
    int northHeight = widgetMap.getOrDefault(NORTH, NULL_WIDGET).getNeededHeight();
    int southHeight = widgetMap.getOrDefault(SOUTH, NULL_WIDGET).getNeededHeight();

    int leftHeight = northHeight + widgetMap.getOrDefault(WEST, NULL_WIDGET).getNeededHeight() + southHeight;
    int middleHeight = northHeight + widgetMap.getOrDefault(CENTER, NULL_WIDGET).getNeededHeight() + southHeight;
    int rightHeight = northHeight + widgetMap.getOrDefault(EAST, NULL_WIDGET).getNeededHeight() + southHeight;
    return Math.max(leftHeight, Math.max(middleHeight, rightHeight));
  }

  @Override
  public int getNeededContentWidth() {
    int northWidth = widgetMap.getOrDefault(NORTH, NULL_WIDGET).getNeededWidth();
    int southWidth = widgetMap.getOrDefault(SOUTH, NULL_WIDGET).getNeededWidth();
    int middleWidth = widgetMap.getOrDefault(WEST, NULL_WIDGET).getNeededWidth() + widgetMap.getOrDefault(CENTER, NULL_WIDGET)
        .getNeededWidth() + widgetMap.getOrDefault(EAST, NULL_WIDGET).getNeededWidth();
    return Math.max(northWidth, Math.max(southWidth, middleWidth));
  }
}
