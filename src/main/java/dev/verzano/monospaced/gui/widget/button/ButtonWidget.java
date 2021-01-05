package dev.verzano.monospaced.gui.widget.button;

import dev.verzano.monospaced.gui.ansi.Attribute;
import dev.verzano.monospaced.gui.constant.Orientation;
import dev.verzano.monospaced.gui.constant.Position;
import dev.verzano.monospaced.gui.task.NamedTask;
import dev.verzano.monospaced.gui.widget.text.TextWidget;
import dev.verzano.monospaced.gui.constant.Key;

public class ButtonWidget extends TextWidget {
  private NamedTask onPressTask;

  public ButtonWidget(NamedTask onPressTask, Position textPosition) {
    super(onPressTask.getName(), Orientation.HORIZONTAL, textPosition);
    this.onPressTask = onPressTask;
    addKeyAction(Key.ENTER, () -> this.onPressTask.fire());
    getFocusedFormat().setAttributes(Attribute.INVERSE_ON);
    getUnfocusedFormat().setAttributes(Attribute.NORMAL);
  }
}
