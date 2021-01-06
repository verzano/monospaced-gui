package dev.verzano.monospaced.gui.container.shelf;

import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.container.ContainerOptions;

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
