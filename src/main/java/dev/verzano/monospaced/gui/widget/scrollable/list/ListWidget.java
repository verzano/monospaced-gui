package dev.verzano.monospaced.gui.widget.scrollable.list;

import dev.verzano.monospaced.core.ansi.sgr.Attribute;
import dev.verzano.monospaced.core.ansi.sgr.Background;
import dev.verzano.monospaced.core.ansi.sgr.Foreground;
import dev.verzano.monospaced.core.ansi.sgr.SgrFormat;
import dev.verzano.monospaced.core.constant.Direction;
import dev.verzano.monospaced.core.constant.Keys;
import dev.verzano.monospaced.gui.MonospacedGui;
import dev.verzano.monospaced.gui.widget.scrollable.ScrollableWidget;
import dev.verzano.monospaced.gui.widget.scrollable.list.model.ListModel;
import dev.verzano.monospaced.gui.widget.scrollable.list.model.Stringable;

import java.util.Collection;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

public class ListWidget<T extends Stringable> extends ScrollableWidget {
    private ListModel<T> listModel;
    private int selectedItemIndex;
    private SgrFormat selectedItemFormat = new SgrFormat(Background.NONE, Foreground.NONE, Attribute.INVERSE_ON);

    public ListWidget(ListModel<T> listModel) {
        setListModel(listModel);

        addKeyAction(Keys.UP_ARROW, () -> {
            scroll(Direction.UP, 1);
            reprint();
        });
        addKeyAction(Keys.DOWN_ARROW, () -> {
            scroll(Direction.DOWN, 1);
            reprint();
        });
    }

    public ListModel<T> getListModel() {
        return listModel;
    }

    public void setListModel(ListModel<T> listModel) {
        this.listModel = listModel;
        selectedItemIndex = 0;
        setViewTop(0);
        setInternalHeight(this.listModel.getItemCount());
    }

    public T getSelectedItem() {
        return listModel.getItemAt(selectedItemIndex);
    }

    public SgrFormat getSelectedItemFormat() {
        return selectedItemFormat;
    }

    public void setSelectedItemFormat(SgrFormat selectedItemFormat) {
        this.selectedItemFormat = selectedItemFormat;
    }

    public void removeItem(T item) {
        if (listModel.removeItem(item)) {
            if (selectedItemIndex == listModel.getItemCount()) {
                selectedItemIndex = Math.max(0, selectedItemIndex - 1);
            }
            setInternalHeight(listModel.getItemCount());
        }
    }

    public void addItem(T item) {
        if (listModel.addItem(item)) {
            if (listModel.getItemCount() > 1 && listModel.getItemIndex(item) <= selectedItemIndex) {
                selectedItemIndex++;
            }
            setInternalHeight(listModel.getItemCount());
        }
    }

    public void setItems(Collection<T> items) {
        listModel.setItems(items);
        selectedItemIndex = 0;
        setViewTop(0);
        setInternalHeight(listModel.getItemCount());
    }

    @Override
    public int getNeededContentHeight() {
        return listModel.getItemCount();
    }

    @Override
    public int getNeededContentWidth() {
        return listModel.getItems().stream().mapToInt(item -> item.stringify().length()).max().orElse(0) + 1;
    }

    @Override
    public void scroll(Direction dir, int distance) {
        switch (dir) {
            case UP -> {
                if (getViewTop() == selectedItemIndex) {
                    setViewTop(Math.max(0, getViewTop() - 1));
                }
                selectedItemIndex = Math.max(0, selectedItemIndex - distance);
            }
            case DOWN -> {
                selectedItemIndex = Math.min(listModel.getItemCount() - 1, selectedItemIndex + distance);
                if (selectedItemIndex == getViewTop() + getHeight()) {
                    setViewTop(Math.min(getViewTop() + 1, listModel.getItemCount() - getHeight()));
                }
            }
        }
    }

    @Override
    public void printContent() {
        super.printContent();

        var width = getContentWidth() - 1;

        for (var row = 0; row < getContentHeight(); row++) {
            MonospacedGui.move(getContentX(), getContentY() + row);
            var index = row + getViewTop();

            if (index >= listModel.getItemCount()) {
                MonospacedGui.printn(" ", width);
            } else {
                var toPrint = listModel.getItemAt(index).stringify();
                if (toPrint.length() > width) {
                    toPrint = toPrint.substring(0, width);
                } else if (toPrint.length() < width) {
                    toPrint = toPrint + new String(new char[width - toPrint.length()]).replace('\0', ' ');
                }

                if (index == selectedItemIndex) {
                    toPrint = selectedItemFormat.getFormatString() + toPrint + normalSgrFormat();
                }

                MonospacedGui.print(toPrint);
            }
        }
    }
}
