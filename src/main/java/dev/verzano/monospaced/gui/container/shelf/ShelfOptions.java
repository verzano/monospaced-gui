package dev.verzano.monospaced.gui.container.shelf;

import dev.verzano.monospaced.gui.container.ContainerOptions;
import dev.verzano.monospaced.gui.metric.Size;

public class ShelfOptions extends ContainerOptions {
  private Size size;

  public ShelfOptions(Size size) {
    this.size = size;
  }

  public Size getSize() {
    return size;
  }

  public void setSize(Size size) {
    this.size = size;
  }
}
