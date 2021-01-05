package dev.verzano.monospaced.gui.floater.binary;

import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.constant.CardinalDirection;
import dev.verzano.monospaced.gui.constant.Key;
import dev.verzano.monospaced.gui.constant.Orientation;
import dev.verzano.monospaced.gui.constant.Position;
import dev.verzano.monospaced.gui.container.enclosure.EnclosureOptions;
import dev.verzano.monospaced.gui.container.shelf.Shelf;
import dev.verzano.monospaced.gui.container.shelf.ShelfOptions;
import dev.verzano.monospaced.gui.floater.Floater;
import dev.verzano.monospaced.gui.metric.Size;
import dev.verzano.monospaced.gui.metric.Spacing;
import dev.verzano.monospaced.gui.task.NamedTask;
import dev.verzano.monospaced.gui.widget.Widget;
import dev.verzano.monospaced.gui.widget.button.ButtonWidget;

public class BinaryChoiceFloater extends Floater {
  private ButtonWidget positiveButton;
  private ButtonWidget negativeButton;
  private Widget displayWidget;
  private boolean positiveSelected;

  public BinaryChoiceFloater(Widget displayWidget, String positiveText, String negativeText) {
    setDisplayWidget(displayWidget);

    Shelf buttonContainer = new Shelf(Orientation.HORIZONTAL, 1);
    buttonContainer.setPadding(new Spacing(1));

    positiveButton = new ButtonWidget(new NamedTask(positiveText) {
      @Override
      public void fire() {
        positiveSelected = true;
        dispose();
      }
    }, Position.CENTER);
    buttonContainer.addWidget(positiveButton, new ShelfOptions(new Size(Size.FILL_NEEDED, Size.FILL_NEEDED)));

    negativeButton = new ButtonWidget(new NamedTask(negativeText) {
      @Override
      public void fire() {
        positiveSelected = false;
        dispose();
      }
    }, Position.CENTER);
    buttonContainer.addWidget(negativeButton, new ShelfOptions(new Size(Size.FILL_NEEDED, Size.FILL_NEEDED)));

    positiveButton.addKeyAction(Key.TAB, () -> {
      negativeButton.setFocused();
      MonospacedGui.reprint();
    });
    negativeButton.addKeyAction(Key.TAB, () -> {
      getDisplayWidget().setFocused();
      MonospacedGui.reprint();
    });

    addWidget(buttonContainer, new EnclosureOptions(CardinalDirection.SOUTH));
  }

  public Widget getDisplayWidget() {
    return displayWidget;
  }

  public ButtonWidget getNegativeButton() {
    return negativeButton;
  }

  public ButtonWidget getPositiveButton() {
    return positiveButton;
  }

  public void setDisplayWidget(Widget displayWidget) {
    this.displayWidget = displayWidget;
    addWidget(displayWidget, new EnclosureOptions(CardinalDirection.CENTER));
  }

  public boolean isPositiveSelected() {
    return positiveSelected;
  }

  @Override
  public void setFocused() {
    displayWidget.setFocused();
  }
}
