package dev.verzano.monospaced.gui.widget.button;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.core.constant.Orientation;
import dev.verzano.monospaced.core.constant.Position;
import dev.verzano.monospaced.gui.task.NamedTask;
import dev.verzano.monospaced.gui.widget.text.TextWidget;

public class ButtonWidget extends TextWidget {
    public ButtonWidget(NamedTask onPressTask, Position textPosition) {
        super(onPressTask.getName(), Orientation.HORIZONTAL, textPosition);
        addKeyAction(Keys.ENTER, onPressTask);
        getFocusedFormat().setAttributes(Attribute.INVERSE_ON);
        getUnfocusedFormat().setAttributes(Attribute.NORMAL);
    }
}
