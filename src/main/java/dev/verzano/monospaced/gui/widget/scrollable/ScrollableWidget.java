package dev.verzano.monospaced.gui.widget.scrollable;

import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.ansi.AnsiFormat;
import dev.verzano.monospaced.gui.ansi.Attribute;
import dev.verzano.monospaced.gui.ansi.Background;
import dev.verzano.monospaced.gui.ansi.Foreground;
import dev.verzano.monospaced.gui.constant.Direction;
import dev.verzano.monospaced.gui.widget.Widget;

import static dev.verzano.monospaced.gui.ansi.AnsiFormat.NORMAL;

// TODO allow this to have vertical and horizontal bars
// TODO make this a container
// TODO make the scrollbar its own widget
// TODO currently scrollbar prints too much when its long
public abstract class ScrollableWidget extends Widget {
  private double internalHeight = 1;
  private AnsiFormat scrollbarFormat = new AnsiFormat(Background.NONE, Foreground.NONE, Attribute.INVERSE_ON);
  private int viewTop = 0;
  private int barLength;

  public abstract void scroll(Direction direction, int distance);

  public ScrollableWidget() {}

  public AnsiFormat getScrollbarFormat() {
    return scrollbarFormat;
  }

  public void setScrollbarFormat(AnsiFormat scrollbarFormat) {
    this.scrollbarFormat = scrollbarFormat;
  }

  public int getViewTop() {
    return viewTop;
  }

  public void setViewTop(int viewTop) {
    this.viewTop = viewTop;
  }

  public void setInternalHeight(double internalHeight) {
    this.internalHeight = internalHeight;
    barLength = (int)Math.ceil(getHeight()*(double)getHeight()/internalHeight);
  }

  @Override
  public void printContent() {
    int barStart = (int)Math.floor(getContentHeight()*(double)viewTop/internalHeight);
    int barEnd = barStart + barLength;

    int x = getContentX() + getContentWidth();
    for(int row = 0; row <= getContentHeight(); row++) {
      MonospacedGui.move(x, getContentY() + row);
      if(row >= barStart && row <= barEnd) {
        MonospacedGui.print(scrollbarFormat.getFormatString() + " " + NORMAL.getFormatString());
      } else {
        MonospacedGui.print(" ");
      }
    }
  }
}
