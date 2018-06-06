package com.verzano.monospaced.gui.container.shelf;

import com.verzano.monospaced.gui.constant.Orientation;
import com.verzano.monospaced.gui.container.Container;
import com.verzano.monospaced.gui.metric.Size;
import com.verzano.monospaced.gui.widget.Widget;

import java.util.*;

public class Shelf extends Container<ShelfOptions> {
  private List<Widget> widgetStack = new LinkedList<>();
  private Map<Widget, ShelfOptions> optionsMap = new HashMap<>();
  private Orientation orientation;
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
  public void addWidgetInternal(Widget widget, ShelfOptions options) {
    optionsMap.put(widget, options);
    widgetStack.add(widget);
  }

  @Override
  public int calculateWidgetHeight(Widget widget) {
    int height = optionsMap.get(widget).getSize().getHeight();

    switch(height) {
      case Size.FILL_CONTAINER:
        return getHeight();
      case Size.FILL_NEEDED:
        return widget.getNeededHeight();
      default:
        return height;
    }
  }

  @Override
  public int calculateWidgetWidth(Widget widget) {
    int width = optionsMap.get(widget).getSize().getWidth();

    switch(width) {
      case Size.FILL_CONTAINER:
        return getWidth();
      case Size.FILL_NEEDED:
        return widget.getNeededWidth();
      default:
        return width;
    }
  }

  @Override
  public int calculateWidgetX(Widget widget) {
    int x = getX();
    if(orientation == Orientation.HORIZONTAL) {
      for(Widget w : widgetStack) {
        if(widget == w) {
          break;
        }

        x += getWidgetWidth(w) + spacing;
      }
    }
    return x + getPadding().getLeft();
  }

  @Override
  public int calculateWidgetY(Widget widget) {
    int y = getY();
    if(orientation == Orientation.VERTICAL) {
      for(Widget w : widgetStack) {
        if(widget == w) {
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
  public void removeWidgetInternal(Widget widget) {
    optionsMap.remove(widget);
    widgetStack.remove(widget);
  }

  @Override
  public void removeWidgetsInternal() {
    optionsMap.clear();
    widgetStack.clear();
  }

  @Override
  public int getNeededContentHeight() {
    int height = 0;
    if(!widgetStack.isEmpty()) {
      switch(orientation) {
        case HORIZONTAL:
          height = widgetStack.stream().mapToInt(Widget::getHeight).max().orElse(0);
          break;
        case VERTICAL:
          height = (widgetStack.size() - 1)*spacing + widgetStack.stream().mapToInt(Widget::getHeight).sum();
          break;
      }
    }

    return height + getPadding().getTop() + getPadding().getBottom();
  }

  @Override
  public int getNeededContentWidth() {
    int width = 0;
    if(!widgetStack.isEmpty()) {
      switch(orientation) {
        case HORIZONTAL:
          width = (widgetStack.size() - 1)*spacing + widgetStack.stream().mapToInt(Widget::getWidth).sum();
          break;
        case VERTICAL:
          width = widgetStack.stream().mapToInt(Widget::getWidth).max().orElse(0);
          break;
      }
    }
    return width + getPadding().getLeft() + getPadding().getRight();
  }
}
