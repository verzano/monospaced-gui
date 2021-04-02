package dev.verzano.monospaced.gui.container.floor;

import dev.verzano.monospaced.core.metric.Point;
import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.container.ContainerOptions;

public class FloorOptions extends ContainerOptions {
    private final Size size = new Size(Size.FILL_CONTAINER, Size.FILL_CONTAINER);
    private final Point location =  new Point(1, 1);

    public FloorOptions() {
    }

    public Point getLocation() {
        return location;
    }

    public Size getSize() {
        return size;
    }
}
