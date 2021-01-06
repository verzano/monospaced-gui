package dev.verzano.monospaced.gui.container.floor;

import dev.verzano.monospaced.core.metric.Point;
import dev.verzano.monospaced.core.metric.Size;
import dev.verzano.monospaced.gui.container.ContainerOptions;

public class FloorOptions extends ContainerOptions {
    private Size size;
    private Point location;

    public FloorOptions(Size size, Point location) {
        this.size = size;
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
