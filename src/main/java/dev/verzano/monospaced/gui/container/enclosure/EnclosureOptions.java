package dev.verzano.monospaced.gui.container.enclosure;

import dev.verzano.monospaced.gui.constant.CardinalDirection;
import dev.verzano.monospaced.gui.container.ContainerOptions;

public class EnclosureOptions extends ContainerOptions {
  private CardinalDirection position;

  public EnclosureOptions(CardinalDirection position) {
    this.position = position;
  }

  public CardinalDirection getPosition() {
    return position;
  }

  public void setPosition(CardinalDirection position) {
    this.position = position;
  }
}
